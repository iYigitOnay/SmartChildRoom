router.post('/register', async (req, res) => {
  try {
    const { username, password } = req.body;

    // Kullanıcı adı ve şifre kontrolü
    if (!username || !password) {
      return res.status(400).json({ message: 'Kullanıcı adı ve şifre zorunludur.' });
    }

    // Aynı kullanıcı var mı kontrol
    const existingUser = await User.findOne({ username });
    if (existingUser) return res.status(400).json({ message: 'Kullanıcı zaten var' });

    // Şifreyi hashle
    const hashedPassword = await bcrypt.hash(password, 10);

    // Kullanıcıyı oluştur, şifreyi hash’lenmiş olarak koy
    const user = new User({ username, password: hashedPassword });
    await user.save();

    res.status(201).json({ message: 'Kayıt başarılı' });
  } catch (err) {
    res.status(500).json({ error: 'Sunucu hatası' });
  }
});
