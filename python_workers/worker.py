import os
import pika
import requests
from bs4 import BeautifulSoup
from tenacity import (
    retry,
    stop_after_attempt,
    wait_fixed,
    RetryError
)
from db import save_scraped_data

WORKER_ID = os.getpid()

QUEUE_NAME = "scrape_jobs_queue"

connection = pika.BlockingConnection(
    pika.ConnectionParameters(
        host='localhost',
        credentials=pika.PlainCredentials(
            'guest',
            'RabbitAdmin'
        )
    )
)

channel = connection.channel()

channel.queue_declare(
    queue=QUEUE_NAME,
    durable=True
)

print(f"Worker {WORKER_ID} waiting for messages...")

@retry(
    stop=stop_after_attempt(3),
    wait=wait_fixed(2)
)
def scrape_website(url):

    try:

        response = requests.get(
            url,
            timeout=10
        )

        response.raise_for_status()

        print(f"\nStatus Code: {response.status_code}")

        soup = BeautifulSoup(
            response.text,
            "html.parser"
        )

        title = soup.title.string if soup.title else "No Title"

        print(f"Page Title: {title}")

        save_scraped_data(
            url,
            title,
            response.status_code
        )

    except requests.Timeout:

        print("Request Timed Out")
        raise

    except requests.ConnectionError:

        print("Connection Error / DNS Failure")
        raise

    except requests.InvalidURL:

        print("Invalid URL")
        raise

    except Exception as e:

        print(f"Unknown Error: {e}")
        raise

def callback(ch, method, properties, body):

    url = body.decode()

    print(
    f"\nWorker {WORKER_ID} received URL: {url}"
    )

    try:

        scrape_website(url)

    except RetryError:

        print(
            f"FINAL FAILURE AFTER RETRIES: {url}"
        )

    except Exception as e:

        print(f"Unexpected Worker Error: {e}")

channel.basic_consume(
    queue=QUEUE_NAME,
    on_message_callback=callback,
    auto_ack=True
)

channel.start_consuming()