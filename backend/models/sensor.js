const mongoose = require("mongoose");

const sensorSchema = new mongoose.Schema({
  kullaniciId: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
  temperature: Number,
  humidity: Number,
  co2: Number,
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model("Sensor", sensorSchema);
