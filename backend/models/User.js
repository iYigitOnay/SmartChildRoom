const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  name: { type: String, required: true },
  surname: { type: String, required: true },
  childName: { type: String, required: true },
  childGender: { type: String, required: true },
  childAge: { type: Number, required: true },
  childBirthDate: { type: Date, required: true },
  emergencyContact: { type: String, required: true },
  sleepTime: { type: String, required: true }
});

module.exports = mongoose.model('User', userSchema);
