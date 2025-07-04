// routes/sensors.js
const express = require("express");
const router = express.Router();
const Sensor = require("../models/sensor");

router.post("/add", async (req, res) => {
  try {
    const { userId, temperature, humidity, co2 } = req.body;

    if (!userId || temperature == null || humidity == null || co2 == null) {
      return res.status(400).json({ error: "Eksik veri gönderildi." });
    }

    const newSensorData = new Sensor({
      kullaniciId: userId, // 🟢 DÜZGÜN KAYIT
      temperature,
      humidity,
      co2
    });


    await newSensorData.save();

    res.status(201).json({ message: "Sensör verisi başarıyla kaydedildi." });
  } catch (err) {
    console.error("❌ Sensör verisi kaydedilirken hata:", err);
    res.status(500).json({ error: "Sunucu hatası", detay: err.message });
  }
});

module.exports = router;
