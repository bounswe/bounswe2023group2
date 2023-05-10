if [ $# -eq 0 ]
then
	uvicorn main:app --host 0.0.0.0 --port 8000 --reload
elif [ $1 = "test" ]
then
	uvicorn main:app --host 0.0.0.0 --port 8000 --reload &
	sleep 2
	echo TESTING...
	python3 tests.py -v
fi
