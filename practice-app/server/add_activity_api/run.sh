sudo docker build -t cahid/addactivity:1.1 . || exit

# Use this line to run the server
sudo docker run -it -p 8000:8000 cahid/addactivity:1.1

# Use this line to run the tests
# sudo docker run -it -p 8000:8000 cahid/addactivity:1.1 test

