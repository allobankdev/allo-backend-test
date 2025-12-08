Allo Bank Backend Developer Take-Home Test (Steven Leonardo)
Untuk setup dan build melakukan step berikut
1. Clone repository fork dari link yang sudah diberikan
2. modify dan setting untuk application.yml untuk setting nama aplikasi, setting url dan hit api nanti nya
3. lakukan build dengan clean install dengan mvn clean install
4. gunakan command mvn-springboot run untuk jalankan aplikasi dan contoh cUrl untuk end api tersebut adalah sebagai berikut:
	latest_idr_rates - 'http://localhost:8080/api/finance/data/latest_idr_rates'
	historical_idr_usd - 'http://localhost:8080/api/finance/data/historical_idr_usd'
	supported_currencies - 'http://localhost:8080/api/finance/data/supported_currencies'
5. lalu gunakan usergithubname : "weakbaby2" untuk SpreadFactor ini adalah dengan total 888 : 119 + 101 + 97 + 107 + 98 + 97 + 98 + 121 + 50

Spread Factor = (Total % 1000) / 100000.0 = 888 / 100000.0 = 0.00888

Penjelasan arsitektur untuk di atas:
Polymorphism Justification: Explain why the Strategy Pattern was used over a simpler conditional block in the service layer for handling the multi-resource endpoint. Discuss the benefits in terms of extensibility and maintainability.

1. Alasan menggunakan strategy pattern adalah karena akan memudahkan dalam suatu aplikasi ketika akan dilakukan debug dapat ditrace dan dilihiat dalam segi error 	   dimana
2. tiap class/fetcher dalam suatu file dapat berkerja secara independen dan ketika nanti ada penambahan atau update suatu fitur dapat dilakukan dengan mudah tanpa merusak bagian yang lain 
3. dengan ada nya fitur injection, springboot dapat menjalankan aplikasi di dalam nya secara otomatis tanpa haru dideclare secara satu per satu.

Client Factory: Explain the specific role and benefit of using a FactoryBean to construct the external API client. Why is this preferable to defining the client using a standard @Bean method in this scenario?

1.FactoryBean dapat menciptakan objek berdasarkan pengaturan saat runtime atau menghasilkan beberapa bean dengan nama atau pengaturan yang berbeda sehingga lebih praktis dibandingkan menulis banyak anotasi @Bean secara manual.
2. FactoryBean dapat diganti di konfigurasi pengujian tanpa perlu merombak bagian kode lainnya.
3.FactoryBean memisahkan proses dengan bean lain nya dari pengguna objek tersebut. Apabila proses pembuatan memerlukan langkah terpisah, semua hal tersebut  tertampung dalam satu lokasi.

Startup Runner Choice: Justify the choice of using an ApplicationRunner (or CommandLineRunner) for the initial data ingestion over a simpler @PostConstruct method.

untuk case ini menggunakan ApplicationRunner lebih baik dibanding postConstruct dikarenakan beberapa hal:
1.Lebih mudah untuk pengujian dan simulasi. Saat pengaktifan runner bisa dinonaktifkan melalui konfigurasi, atau dipasangi mock untuk pengujian integrasi tanpa mengganggu siklus inisialisasi bean.
2.Tidak menghalangi kerja bean lainnya sehingga dapat kemungkinan untuk menjalankan secara asinkron jika diperlukan sementara @PostConstruct seringkali menjadi tempat yang menarik untuk meletakkan ide bisnis/logic yng compleks, yang bisa memperlambat atau menyulitkan proses startup.
