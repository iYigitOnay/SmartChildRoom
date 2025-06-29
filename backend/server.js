const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
require("dotenv").config();

const Sensor = require("./models/Sensor"); // 👈 BURAYA EKLİYORSUN

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

// Ana test endpoint
app.get("/", (req, res) => {
  res.send("Akıllı Çocuk Odası Backend Çalışıyor ✅");
});

// Route tanımını en başa alabiliriz
const userRoutes = require("./routes/user");
app.use("/api/user", userRoutes);

// Dashboard endpoint
app.get("/dashboard/:kullaniciAdi", async (req, res) => {
  const { kullaniciAdi } = req.params;

  try {
    // Kullanıcı bilgilerini çek (user modelini de eklemen lazım)
    const user = await User.findOne({ kullaniciAdi });
    if (!user) {
      return res.status(404).json({ mesaj: "Kullanıcı bulunamadı" });
    }

    // En son sensör verisini al
    const sensorDataArr = await Sensor.find({ kullaniciId: kullaniciAdi })
                                      .sort({ createdAt: -1 })
                                      .limit(1);

    const sensorData = sensorDataArr[0] || {};

    // Dashboard'da ihtiyacın olan tüm bilgileri birleştir
    const dashboardResponse = {
      userName: user.name,
      childName: user.childName,
      emergencyContact: user.emergencyPhone,
      sleepSchedule: user.sleepSchedule,
      childBirthDate: user.childBirthDate,
      sensorData: {
        temperature: sensorData.temperature,
        humidity: sensorData.humidity,
        co2: sensorData.co2
      }
    };

    res.status(200).json(dashboardResponse);
  } catch (error) {
    console.error("Dashboard veri hatası:", error);
    res.status(500).json({ mesaj: "Sunucu hatası" });
  }
});


// MongoDB bağlantısı ve server başlatma
mongoose
  .connect(process.env.MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
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
