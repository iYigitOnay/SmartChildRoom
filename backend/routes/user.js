const express = require("express");
const router = express.Router();
const User = require("../models/user"); // User modelini dahil et

// Kullanıcı kayıt endpoint'i
router.post("/register", async (req, res) => {
  try {
    const user = new User(req.body); // İstekten gelen verileri User modeline at
    await user.save(); // MongoDB'ye kaydet
    res.status(201).json({ message: "Kullanıcı başarıyla kaydedildi ✅" });
  } catch (error) {
    res
      .status(500)
      .json({ error: "Kayıt başarısız ❌", detail: error.message });
  }
});

module.exports = router;
