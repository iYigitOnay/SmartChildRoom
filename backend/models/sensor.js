const mongoose = require("mongoose");

const sensorSchema = new mongoose.Schema({
  temp: Number, // ✅ Veritabanına uygun alan adı
  hum: Number,
  co2: Number,
  date: {
    type: Date,
    default: Date.now
  },
kullaniciId: { type: String },
  createdAt: {
    type: Date,
    default: Date.now
  }
});

module.exports = mongoose.model("Sensor", sensorSchema);
