const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
require("dotenv").config(); // .env dosyasından gizli bilgileri okuyacak

const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use(express.json()); // JSON body verisini okuyabilmek için

// Ana endpoint (test)
app.get("/", (req, res) => {
  res.send("Akıllı Çocuk Odası Backend Çalışıyor ✅");
});

// MongoDB bağlantısı
mongoose
  .connect(process.env.MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => {
    console.log("✅ MongoDB bağlantısı başarılı");
    app.listen(PORT, () => {
      console.log(`🚀 Server http://localhost:${PORT} adresinde çalışıyor`);
    });
  })
  .catch((err) => {
    console.error("❌ MongoDB bağlantı hatası:", err);
  });

// User route'unu dahil et
const userRoutes = require("./routes/user");
app.use("/api/user", userRoutes);
