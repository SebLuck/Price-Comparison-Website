//Import the express and url modules
const express = require('express');
const url = require("url");
const bodyParser = require('body-parser');
//Status codes defined in an external file
require('./http_status.js');

//The express module is a function. When it is executed it returns an app object
const app = express();
app.use(bodyParser.json());
// http with express
const http = require("http").createServer(app);
//Import the mysql module
const mysql = require('mysql');

//Create a connection object with the user details
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "Seb",
    password: "*****",
    database: "price_comparison",
    port: 8111,
    debug: false
});

//Set up the application to handle GET requests sent to the user's path.
app.get('/laptops/*', handleGetRequest);//Subfolders
app.get('/laptops', handleGetRequest);
app.get('/search/*', handleGetRequest);
app.get('/search', handleGetRequest);

//Serve up static pages from the public folder.
app.use(express.static('public'));

/* Handles GET requests sent to web service.
   Processes path and query string and calls appropriate functions to
   return the data. */
function handleGetRequest(request, response){
    //Parse the URL
    let urlObj = url.parse(request.url, true);

    //Extract object containing queries from URL object.
    let queries = urlObj.query;

    //Get the pagination properties if they have been set. Will be undefined if not set.
    let numItems = queries['num_items'];
    let offset = queries['offset'];

    //Split the path of the request into its components.
    let pathArray = urlObj.pathname.split("/");

    //Get the last part of the path
    let pathEnd = pathArray[pathArray.length - 1];
    //If path ends with 'laptops' we return all laptops

    if(pathEnd === 'laptops'){
        getTotalLaptopsCount(response, numItems, offset);//This function calls the getAllLaptops function in its callback
        return;
    }
    //If path ends with laptops/, we return all laptops
    if (pathEnd === '' && pathArray[pathArray.length - 2] === 'laptops'){
        getTotalLaptopsCount(response, numItems, offset);//This function calls the getAllLaptops function in its callback
        return;
    }
    //If the last part of the path is a valid user ID, return data about that user.
    var regEx = new RegExp('^[0-9]+$');//RegEx returns true if the string is all digits.
    // Remove %20 from pathEnd and replace it by a space using decodeURIComponent.
    pathEnd = decodeURIComponent(pathEnd);
    if(regEx.test(pathEnd)){
        getLaptop(response, pathEnd);
        return;
    }
    
    if(pathArray[pathArray.length - 2] === 'search'){
        getTotalLaptopSearch(response, pathEnd, numItems, offset);
        return;
    }

    //The path is not recognized. Return an error message
    response.status(HTTP_STATUS.NOT_FOUND);
    response.send("{error: 'Path not recognized', url: " + request.url + "}");
}


/** Returns all of the laptops, possibly with a limit on the total number of items returned and the offset (to enable pagination).
 *  This function should be called in the callback of getTotalLaptopsCount. */
 function getAllLaptops(response, totNumItems, numItems, offset){
    //Select the laptop's data using JOIN to convert foreign keys into useful data.
    let sql1 = "SELECT laptop_comparison.id, laptop_comparison.price, laptop_comparison.url, laptop.brand, laptop.description, laptop.image " +
    "FROM ( (laptop INNER JOIN laptop_comparison ON laptop.id=laptop_comparison.laptop_id)) " +
    "ORDER BY laptop_comparison.id"; 

    //Limit the number of results returned, if this has been specified in the query string.
    if(numItems !== undefined && offset !== undefined ){
        sql1 += " LIMIT " + numItems + " OFFSET " + offset;
    }

    //Execute the query
    connectionPool.query(sql1, function (err, result) {

        //Check for errors
        if (err){
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Create a JavaScript object that combines the total number of items with data.
        let returnObj = {totNumItems: totNumItems};
        returnObj.laptop = result; //Array of data from database
        //Return results in JSON format
        response.json(returnObj);
    });
}
/** When retrieving all laptops we start by retrieving the total number of laptops
    The database callback function will then call the function to get the data
    with pagination */
function getTotalLaptopsCount(response, numItems, offset){
    let sql2 = "SELECT COUNT(*) FROM laptop";

    //Execute the query and call the anonymous callback function.
    connectionPool.query(sql2, function (err, result) {

        //Check for errors
        if (err){
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Get the total number of items from the result
        let totNumItems = result[0]['COUNT(*)'];

        //Call the function that retrieves all laptops.
        getAllLaptops(response, totNumItems, numItems, offset);
    });
}

/** Returns the laptop with the specified ID */
function getLaptop(response, id){
    //Build SQL query to select laptop with specified id.
    let sql3 = "SELECT laptop_comparison.id, laptop_comparison.price, laptop_comparison.url, laptop.brand, laptop.description, laptop.image " +
              "FROM ( (laptop INNER JOIN laptop_comparison ON laptop.id=laptop_comparison.laptop_id)) " +
              "WHERE laptop_comparison.id=" + id;
    //Execute the query
    connectionPool.query(sql3, function (err, result) {
        //Check for errors
        if (err){
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }
        //Output results in JSON format
        response.json(result);
    });
}
// function to search by description and brand
function searchLaptop(response, searchTerm, numItems, offset, totNumItems){
    let sql4 = "SELECT laptop_comparison.id, laptop_comparison.price, laptop_comparison.url, laptop.brand, laptop.description, laptop.image " +
    "FROM ( (laptop INNER JOIN laptop_comparison ON laptop.id=laptop_comparison.laptop_id)) " +
    "WHERE CONCAT_WS('', brand, description) LIKE '%" + searchTerm  + "%' ";

    //Limit the number of results returned if this has been specified in the query string.
    if(numItems !== undefined && offset !== undefined ){
        sql4 += "ORDER BY laptop_comparison.id LIMIT " + numItems + " OFFSET " + offset;
    }
    //Execute the query
    connectionPool.query(sql4, function (err, result) {
        //Check for errors
        if (err){
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }
        //Create JavaScript object that combines total number of items with data
        let returnObj = {totNumItems: totNumItems};
        returnObj.laptop = result; //Array of data from database
        //Return results in JSON format
        response.json(returnObj);
    });
}
// function to get the total laptops from the search
function getTotalLaptopSearch(response, searchTerm, numItems, offset){
    let sql5 = "SELECT COUNT(*) FROM laptop " +
               "WHERE CONCAT_WS('', brand, description) LIKE '%" + searchTerm + "%' ";
        //Execute the query and call the anonymous callback function.
    connectionPool.query(sql5, function (err, result) {

        //Check for errors
        if (err){
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }
        //Get the total number of items from the result
        let totNumItems = result[0]['COUNT(*)'];
        searchLaptop(response, searchTerm, numItems, offset, totNumItems);
    });
}
//Start the app listening on port 8080
http.listen(8080, function(){
    console.log("Listening on port 8080");
});

//Export server for testing
module.exports = app;
