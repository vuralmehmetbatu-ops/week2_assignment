# week2_assignment — DevCluster

Jetpack Compose ile hazırlanmış Donanım ve Sunucu Kaynak Yönetim Simülatörü.

## Ödev gereklilikleri
- `HardwareItem` immutable data class ve `@Immutable`
- Dinamik toplam bütçe durumu `remember`
- Arama durumu `rememberSaveable`
- Filtreleme `remember(searchQuery)` ile memoization
- Liste `LazyColumn`
- `items(..., key = { it.id })`
- `HardwareCard` ve `HardwareSearchBar` stateless
- State Down / Events Up
- Kart modifier sırası: `clickable()` ardından `padding()`
- Verilen 8 elemanlık sabit veri kümesi

## Açma
Android Studio ile proje kök klasörünü açın ve Gradle Sync tamamlandıktan sonra `app` yapılandırmasını çalıştırın.
