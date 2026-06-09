import psycopg2

connection = psycopg2.connect(
    host="localhost",
    database="scraperdb",
    user="postgres",
    password="Aman2003",
    port="5432"
)

connection.autocommit = True

cursor = connection.cursor()

cursor.execute("""

CREATE TABLE IF NOT EXISTS scraped_data (

    id SERIAL PRIMARY KEY,

    url TEXT,

    page_title TEXT,

    status_code INTEGER,

    scraped_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

)

""")

def save_scraped_data(
    url,
    page_title,
    status_code
):

    cursor.execute(
        """
        INSERT INTO scraped_data
        (url, page_title, status_code)
        VALUES (%s, %s, %s)
        """,
        (
            url,
            page_title,
            status_code
        )
    )

    print("Data saved to PostgreSQL")