# Sermon App 📖

Diyanet İşleri Başkanlığı'nın hutbelerini görüntüleyin.

## Özellikler ✨

- 📱 MVVM mimarisi
- 🔔 Firebase Cloud Messaging ile push notification desteği
- 💎 Premium içerik (Google Play Billing)
- 🧭 Kıble pusulası
- 📅 Özel günler
- 🌙 Karanlık/Aydınlık tema desteği

## Teknolojiler 🛠️

- **Mimari**: MVVM (Model-View-ViewModel)
- **UI**: Databinding, CollapsingToolbar, Jetpack Compose
- **Network**: Retrofit, RxJava
- **Image Loading**: Glide
- **Push Notifications**: Firebase Cloud Messaging (FCM)
- **Billing**: Google Play Billing Library
- **Location**: Google Play Services Location

## Firebase Cloud Messaging Kurulumu 🔔

Uygulama, yeni hutbe eklendiğinde kullanıcılara bildirim gönderebilir.

### Hızlı Başlangıç:

1. Firebase Console'dan `google-services.json` dosyasını indirin
2. Dosyayı `app/` klasörüne kopyalayın
3. Detaylı kurulum için [FCM_SETUP_GUIDE.md](FCM_SETUP_GUIDE.md) dosyasına bakın

### Bildirim Gönderme:

**Manuel (Firebase Console):**
- Firebase Console > Messaging > New campaign
- Topic: `sermons`

**Programatik (Python Script):**
```bash
python send_notification.py
```

Detaylı bilgi için [FCM_SETUP_GUIDE.md](FCM_SETUP_GUIDE.md) dosyasını inceleyin.

## Kurulum 📦

1. Projeyi klonlayın
2. Android Studio'da açın
3. Firebase kurulumunu yapın (yukarıya bakın)
4. Projeyi derleyin ve çalıştırın

## Lisans 📄

Bu proje kişisel kullanım içindir.
