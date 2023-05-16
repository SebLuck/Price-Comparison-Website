//Server code that we are testing
let server = require ('../server')
// //Database code that we are testing
// let db = require('../database_test');
//Set up Chai library 
let chai = require('chai');
let should = chai.should();
let assert = chai.assert;
let expect = chai.expect;

//Set up Chai for testing web service
let chaiHttp = require ('chai-http');
chai.use(chaiHttp);

//Import the mysql module and create a connection pool with the user details
const mysql = require('mysql');
const res = require('../node_modules/express/lib/response');
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "Seb",
    password: "*****",
    database: "price_comparison",
    port: 8111,
    debug: false
});

describe('#getLaptop', () => {
    it('should get Id of a laptop', (done) => {
        //Check that user has been added to database
        let sql = "SELECT laptop_comparison.id, laptop_comparison.price, laptop_comparison.url, laptop.brand, laptop.description, laptop.image " +
        "FROM ( (laptop INNER JOIN laptop_comparison ON laptop.id=laptop_comparison.laptop_id)) " +
        "WHERE laptop_comparison.id=1";
        connectionPool.query(sql, (err, res) => {
            if (err){//Check for errors
                assert.fail(err);//Fail test if this does not work.
                done();//End test
            }
            else{
                connectionPool.query(sql, (err, res) => {
                    if (err){//Check for errors
                        assert.fail(err);//Fail test if this does not work.
                        done();//End test
                    }
                    else{
                        done();//End test
                    }
                });
            }
        });
    });
});

describe('#getAllLaptops', () => {
    it('should returns all the laptops', (done) => {
        //Check that user has been added to database
        let sql = "SELECT laptop_comparison.id, laptop_comparison.price, laptop_comparison.url, laptop.brand, laptop.description, laptop.image " +
        "FROM ( (laptop INNER JOIN laptop_comparison ON laptop.id=laptop_comparison.laptop_id)) " +
        "ORDER BY laptop_comparison.id";

        connectionPool.query(sql, (err, res) => {
            if (err){//Check for errors
                assert.fail(err);//Fail test if this does not work.
                done();//End test
            }
            else{
                connectionPool.query(sql, (err, res) => {
                    if (err){//Check for errors
                        assert.fail(err);//Fail test if this does not work.
                        done();//End test
                    }
                    else{
                        done();//End test
                    }
                });
            }
        });
    });
});

describe('#getAllLaptopsByPagination', () => {
    it('should returns all the laptops with a limit on the total number of items returned and the offset', (done) => {
        //Check that user has been added to database
        let sql = "SELECT laptop_comparison.id, laptop_comparison.price, laptop_comparison.url, laptop.brand, laptop.description, laptop.image " +
        "FROM ( (laptop INNER JOIN laptop_comparison ON laptop.id=laptop_comparison.laptop_id)) " +
        "ORDER BY laptop_comparison.id " +
        "LIMIT 24 OFFSET 0";

        connectionPool.query(sql, (err, res) => {
            if (err){//Check for errors
                assert.fail(err);//Fail test if this does not work.
                done();//End test
            }
            else{
                connectionPool.query(sql, (err, res) => {
                    if (err){//Check for errors
                        assert.fail(err);//Fail test if this does not work.
                        done();//End test
                    }
                    else{
                        done();//End test
                    }
                });
            }
        });
    });
});

describe('#getTotalLaptopsCount', () => {
    it('should returns the total number of laptops', (done) => {
        //Check that user has been added to database
        let sql = "SELECT COUNT(*) FROM laptop";

        connectionPool.query(sql, (err, res) => {
            if (err){//Check for errors
                assert.fail(err);//Fail test if this does not work.
                done();//End test
            }
            else{
                connectionPool.query(sql, (err, res) => {
                    if (err){//Check for errors
                        assert.fail(err);//Fail test if this does not work.
                        done();//End test
                    }
                    else{
                        done();//End test
                    }
                });
            }
        });
    });
});

describe('#getTotalLaptopSearch', () => {
    it('should returns the total number of laptops based on the search', (done) => {
        //Check that user has been added to database
        let sql = "SELECT COUNT(*) FROM laptop " +
        "WHERE CONCAT_WS('', brand, description) LIKE '%Lenovo%' ";

        connectionPool.query(sql, (err, res) => {
            if (err){//Check for errors
                assert.fail(err);//Fail test if this does not work.
                done();//End test
            }
            else{
                connectionPool.query(sql, (err, res) => {
                    if (err){//Check for errors
                        assert.fail(err);//Fail test if this does not work.
                        done();//End test
                    }
                    else{
                        done();//End test
                    }
                });
            }
        });
    });
});
