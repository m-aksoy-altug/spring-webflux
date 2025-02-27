FROM mongo:latest
EXPOSE 27017
CMD ["mongod"]
# docker build -t mongo-reactive:7.0.17 .

# docker run -d -p 27017:27017 --name mongo-container \
#  -e MONGO_INITDB_ROOT_USERNAME=admin \
#  -e MONGO_INITDB_ROOT_PASSWORD=adminpassword \
#  -e MONGO_INITDB_DATABASE=reactive \
#  -v /home/altug/Desktop/Mongocontainer:/data/db \
#  mongo-reactive:7.0.17
  
 docker exec -it mongo-container mongosh -u admin -p \
 adminpassword --authenticationDatabase admin

# In Mongo shelll
#show dbs
#use reactive
#show tables
#db.dummy.insertMany([
#    { _id: 1, name: "Item A", highOn: ISODate("2024-02-26"), price: 100 },
#    { _id: 2, name: "Item B", highOn: ISODate("2024-02-25"), price: 200 },
#    { _id: 3, name: "Item C", highOn: ISODate("2024-02-24"), price: 300 }
#])
#db.dummy.find({})
#db.dummy.find({ name: "Item A" })








