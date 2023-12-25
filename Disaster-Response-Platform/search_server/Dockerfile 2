FROM python:3.11

WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt

COPY search_controller.py sen_init.py ./ 

RUN python sen_init.py
EXPOSE 9060
CMD ["uvicorn", "search_controller:app", "--host", "0.0.0.0", "--port", "9060"]