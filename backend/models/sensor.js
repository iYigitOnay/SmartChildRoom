const mongoose = require("mongoose");

const sensorSchema = new mongoose.Schema({
  kullaniciId: { type: String, required: true },
  temperature: Number,
  humidity: Number,
  co2: Number,
  createdAt: {
    type: Date,
    default: Date.now,
    expires: 60 * 60 * 24 * 7, // 7 gün sonra silinir (isteğe bağlı)
  },
});

// ZATEN TANIMLIYSA ONU KULLAN, YOKSA YENİ OLUŞTUR
module.exports = mongoose.models.Sensor || mongoose.model("Sensor", sensorSchema);
