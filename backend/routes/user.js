const express = require("express");
const router = express.Router();
const User = require("../models/user");
const Sensor = require("../models/sensor");
const bcrypt = require("bcrypt");

// Kullanıcı Kayıt
router.post("/register", async (req, res) => {
  try {
    const {
      kullaniciAdi,
      sifre,
      ad,
      soyad,
      cocukAdi,
      cocukCinsiyeti,
      cocukYasi,
      cocukDogumTarihi,
      acilDurumKisisi,
      uykuZamani,
    } = req.body;

    if (!kullaniciAdi || !sifre) {
      return res.status(400).json({ error: "Kullanıcı adı ve şifre zorunludur." });
    }

    const mevcut = await User.findOne({ kullaniciAdi });
    if (mevcut) {
      return res.status(409).json({ error: "Bu kullanıcı adı zaten kayıtlı." });
    }

    const hashed = await bcrypt.hash(sifre, 10);

    const user = new User({
      kullaniciAdi,
      sifre: hashed,
      ad,
      soyad,
      cocukAdi,
      cocukCinsiyeti,
      cocukYasi,
      cocukDogumTarihi,
      acilDurumKisisi,
      uykuZamani,
    });

    await user.save();
    res.status(201).json({ message: "Kullanıcı başarıyla kaydedildi ✅" });
  } catch (err) {
    console.error("Kayıt hatası:", err);
    res.status(500).json({ error: "Kayıt başarısız ❌" });
  }
});

// Kullanıcı Giris
router.post("/login", async (req, res) => {
  try {
    const { kullaniciAdi, sifre } = req.body;

    if (!kullaniciAdi || !sifre) {
      return res.status(400).json({ error: "Kullanıcı adı ve şifre gerekli" });
    }

    const user = await User.findOne({ kullaniciAdi });
    if (!user) return res.status(400).json({ error: "Kullanıcı bulunamadı" });

    const passMatch = await bcrypt.compare(sifre, user.sifre);
    if (!passMatch) return res.status(400).json({ error: "Şifre yanlış" });

    res.status(200).json({ message: "Giriş başarılı ✅", userId: user._id });
  } catch (err) {
    console.error("Login hatası:", err);
    res.status(500).json({ error: "Sunucu hatası" });
  }
});

// Kullanıcı Profili Getir
router.get("/:id/profile", async (req, res) => {
  try {
    const user = await User.findById(req.params.id);
    if (!user) return res.status(404).json({ error: "Kullanıcı bulunamadı" });

    res.json({
      adSoyad: `${user.ad} ${user.soyad}`,
      cocukAdi: user.cocukAdi,
      cocukDogumTarihi: user.cocukDogumTarihi,
      uykuZamani: user.uykuZamani,
      acilDurumKisisi: user.acilDurumKisisi,
    });
  } catch (err) {
    console.error("Profil hatası:", err);
    res.status(500).json({ error: "Sunucu hatası" });
  }
});

// Dashboard Verisi Getir
router.get("/dashboard/:userId", async (req, res) => {
  try {
    const userId = req.params.userId;

    const user = await User.findById(userId);
    if (!user) return res.status(404).json({ error: "Kullanıcı bulunamadı" });

    const sensorData = await Sensor.findOne({ userId }).sort({ createdAt: -1 });

    const dashboardData = {
      userName: user.ad + " " + user.soyad,
      childName: user.cocukAdi,
      childBirthDate: user.cocukDogumTarihi || null,
      sleepSchedule: user.uykuZamani || null,
      emergencyContact: user.acilDurumKisisi || null,
      sensorData: sensorData || {},
    };

    res.json(dashboardData);
  } catch (error) {
    console.error("Dashboard hatası:", error);
    res.status(500).json({ error: "Sunucu hatası" });
  }
});

module.exports = router;
