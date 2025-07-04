const mongoose = require("mongoose");

mongoose.connect("mongodb+srv://ihsanyigit:Mely1128@cluster0.44zzfgr.mongodb.net/birbu?retryWrites=true&w=majority", {
  useNewUrlParser: true,
  useUnifiedTopology: true
});

const db = mongoose.connection;

db.once("open", async () => {
  console.log("✅ MongoDB bağlantısı başarılı");

  const collection = db.collection("sensors");

  const faultyDocs = await collection.find({ kullaniciId: { $type: "string" } }).toArray();

  for (const doc of faultyDocs) {
    const newData = {
      kullaniciId: mongoose.Types.ObjectId(doc.kullaniciId),
      temperature: doc.temp,
      humidity: doc.hum,
      co2: doc.co2 || 0,
      createdAt: doc.date ? new Date(doc.date) : new Date()
    };

    await collection.insertOne(newData);
    await collection.deleteOne({ _id: doc._id });
    console.log("✔️ Dönüştürüldü:", doc._id.toString());
  }

  console.log("🎉 Tüm veriler dönüştürüldü.");
  db.close();
});
