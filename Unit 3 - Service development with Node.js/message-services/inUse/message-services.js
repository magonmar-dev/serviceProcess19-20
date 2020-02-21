const mongoose = require('mongoose');
const express = require('express');
const bodyParser = require('body-parser');
const jwt = require('jsonwebtoken');
const sha256 = require('sha256');
const fs = require('fs');

const secretWord = "DAMsecret";
const filePath = `img/${Date.now()}.jpg`;

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/users', { useNewUrlParser: true, useUnifiedTopology: true });

let userSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        minlength: 1,
        match: /^\w+$/,
        unique: true
    },
    password: {
        type: String,
        required: true,
        minlength: 4
    },
    image: {
        type: String,
        required: true
    }
});

let User = mongoose.model('user', userSchema);

let messageSchema = new mongoose.Schema({
    from: {
        type: mongoose.Schema.Types.ObjectId, 
        ref: 'user',
        required: true
    },
    to: {
        type: mongoose.Schema.Types.ObjectId, 
        ref: 'user',
        required: true
    },
    message: {
        type: String,
        required: true,
        trim: true,
        minlength: 1
    },
    image: {
        type: String,
        required: false
    },
    sent: {
        type: String,
        required: true,
        trim: true,
        minlength: 10
    }
});

let Message = mongoose.model('message', messageSchema);

let app = express();
app.use(bodyParser.json());

let generateToken = _id => {
    let token = jwt.sign({_id: _id}, secretWord, {expiresIn:"1 year"});
    return token;
}

let validateToken = token => {
    try {
        let user = jwt.verify(token, secretWord);
        return user;
    } catch (e) { 
        return null; 
    }
}

// Users services

app.post('/login', (req, res) => {

    // Get user credentials from the request
    let userClient = {
        name: req.body.name,
        password: sha256(req.body.password)
    };

    // Look for user in the collection
    User.findOne({name: userClient.name, password: userClient.password}).then(data => {
        // User is valid. Generate token
        if (data != null) {
            let token = generateToken(data._id);
            let result = {ok: true, token: token, name: userClient.name, image: data.image};
            res.send(result);           
        // User not found, generate error message 
        } else {
            let result = {ok: false, error: "User or password incorrect"};
            res.send(result);
        }
    }).catch (error => {
        // Error searching user. Generate error message
        let result = {ok: false, error: "Error trying to validate user"};
        res.send(result);
    });
});

app.post('/register', (req, res) => {

    if(!req.body.image)
        res.send({error: "Image not found"});

    fs.writeFileSync(filePath, Buffer.from(req.body.image, 'base64'));

    let newUser = new User({
        name: req.body.name,
        password: sha256(req.body.password),
        image: filePath
    });

    newUser.save().then(data => {
        res.send({ok: true});
    }).catch(error => {
        let result = {ok: false, error: "User couldn't be registered"};
        res.send(result);
    })
});

app.get('/users', (req, res) => {

    let token = req.headers['authorization'];

    if (token) {
        if (validateToken(token)) {
            User.find().then(result => {
                res.send({ok: true, users: result});
            }).catch(error => {
                res.send({ok: false, error: "Error listing users"});
            });
        } else {
            let result = {ok: false, error: "Error validating user"};
            res.send(result);                
        }
    } else {
        res.sendStatus(401);
    } 
});

app.put('/users', (req, res) => {

    let user = validateToken(req.headers['authorization']);
    
    if (user) {
        if(!req.body.image) {
            res.send({error: "Image not found"});
        } else {
            User.findById(user._id).then(user => {

                fs.writeFileSync(filePath, Buffer.from(req.body.image, 'base64'));
                
                user.save().then(user => {
                    res.send({ok: true});
                }).catch(error => {
                    res.send({ok: false, error: "Error updating user: " + user._id});
                });
            }).catch (error => {
                res.send({ok: false, error: "User not found"});
            });
        }
    } else {
        res.sendStatus(401);
    }
});


// Messages services

app.get('/messages', (req, res) => {

    let user = validateToken(req.headers['authorization']);

    if (user) {
        Message.find({to: user._id}).then(messages => {
            res.send({ok: true, messages: messages});
        }).catch(error => {
            res.send({ok: false, error: "Error getting messages for user: " + user._id});
        });
    } else {
        res.sendStatus(401);
    }
});

app.post('/messages/:UserId', (req, res) => {

    let user = validateToken(req.headers['authorization']);

    let newMessage = null;

    if (user) {

        if(req.body.image) {
            fs.writeFileSync(filePath, Buffer.from(req.body.image, 'base64'));

            newMessage = new Message({
                from: user._id,
                to: req.params.UserId,
                message: req.body.message,
                image: filePath,
                sent: req.body.sent
            });
        } else {
            newMessage = new Message({
                from: user._id,
                to: req.params.UserId,
                message: req.body.message,
                sent: req.body.sent
            });
        }
    
    newMessage.save().then(message => {
        res.send({ok: true, message: message});
    }).catch(error => {
        res.send({ok: false, error: "Error sending a message to: " + req.params.UserId});
    })

    } else {
        res.sendStatus(401);
    }
});

app.delete('/messages/:id', (req, res) => {

    Message.findByIdAndRemove(req.params.id).then(result => {
        res.send({ok: true});
    }).catch(error => {
        res.send({ok: false, error:"Error deleting message: " + req.params.id});
    });
});

app.use(express.static('public'));
app.use('/img', express.static(__dirname + '/img'));
app.listen(8080);