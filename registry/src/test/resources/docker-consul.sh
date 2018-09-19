
# docker install by docker on MacOS
brew cask install docker

# install consul image
docker pull consul

================================
# consul cluster by docker
# run the first server node expecting at least two more nodes to form a Consul cluster
docker run -d --name node1 -h node1 progrium/consul -server -bootstrap-expect 3

# other two nodes will join the first one using its IP (in Docker network)
JOIN_IP="$(docker inspect -f '{{.NetworkSettings.IPAddress}}' node1)"

# run second server node
docker run -d --name node2 -h node2 progrium/consul -server -join $JOIN_IP

# run third server node
docker run -d --name node3 -h node3 progrium/consul -server -join $JOIN_IP

# run fourth *client* node, this time exposing Consul ports to the host machine
# it'll be able to communicate with the cluster but will not participate in the consensus quorum
docker run -d -p 8400:8400 -p 8500:8500 -p 8600:53/udp --name node4 -h node4 progrium/consul -join $JOIN_IP

================================

# show all images
docker images

# show all ps on docker
docker ps

# list all docker containers
docker exec -t node1 consul members

# docker kill eg.
docker container kill e078e3b371ea 8ed1075e7f60 683d189cab5d
