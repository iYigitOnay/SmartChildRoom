const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
require("dotenv").config();

const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use(express.json());

// Routes
const userRoutes = require("./routes/user");
app.use("/api/user", userRoutes);

const sensorRoutes = require("./routes/sensors");
app.use("/api/sensors", sensorRoutes); // 🔥 Şu an burası çalışacak

// Test endpoint
app.get("/", (req, res) => {
  res.send("Akıllı Çocuk Odası Backend Çalışıyor ✅");
});

// Mongo bağlantısı ve server start
mongoose
  .connect(process.env.MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true
  })
  .then(() => {
    console.log("✅ MongoDB bağlantısı başarılı");
    app.listen(PORT, '0.0.0.0', () => {
      console.log(`🚀 Server http://localhost:${PORT} adresinde çalışıyor`);
    });
  })
  .catch((err) => {
    console.error("❌ MongoDB bağlantı hatası:", err);
  });
