FROM python:3.10.4

RUN mkdir -p /app
WORKDIR /app

COPY ./practice-app/server/requirements.txt /app/requirements.txt
RUN pip install -r requirements.txt

COPY ./practice-app/server/ /app/

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "5000"]