const mongoose = require("mongoose");

const sensorSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: "User" },
  temperature: Number,
  humidity: Number,
  co2: Number,
  sleepSchedule: String,
  emergencyPhone: String,
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model("Sensor", sensorSchema);
