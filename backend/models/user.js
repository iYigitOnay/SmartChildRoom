const mongoose = require("mongoose");

const userSchema = new mongoose.Schema({
  kullaniciAdi: { type: String, required: true, unique: true },
  sifre: { type: String, required: true },
  ad: String,
  soyad: String,
  cocukAdi: String,
  cocukCinsiyeti: String,
  cocukYasi: Number,
  cocukDogumTarihi: String,
  acilDurumKisisi: String,
  uykuZamani: String,
});

module.exports = mongoose.model("User", userSchema);
