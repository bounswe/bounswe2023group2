from fastapi import FastAPI
import psycopg2

app = FastAPI()

@app.get('/')
def home():
    try:
        conn = psycopg2.connect(
            host="172.18.0.2",
            database="postgres",
            user="postgres",
            password="postgres",
            port="5432"
        )
        cur = conn.cursor()

        # execute a query
        cur.execute("SELECT * FROM accounts")

        hasan = ""
        # print the results
        for row in cur:
            for i in row:
                osman += str(i)

        cur.close()
        conn.close()

        print(hasan)
        return {"message": hasan}
    except Exception as e:
        return {"error": str(e)}
