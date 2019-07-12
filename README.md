# Popular-movies-stage2

### Movie information
Movie information is provided by [TheMovieDB](https://developers.themoviedb.org)

### Package structure
Package is divided between component types like:
* api (Retrofit related classes)
* model (Sub divided in domain and remote to separate between classes used inside app and classes that comes from API)
* repository (Room related classes)
* ui (Activities and adapters)
* utils (Enums and Preferences)
* viewmodel (View model related classes)

### Room integration
Using room to save favorite user's movies to be displayed offline. All movie information is saved on it's local database, and [Picasso](https://github.com/square/picasso)
takes care of handling cached image display.

### Youtube integration 
This app uses this library [Youtube Player](https://github.com/PierfrancescoSoffritti/android-youtube-player#get-a-reference-to-youtubeplayer)
to provide a watch experiencie for the user without leaving the app.
The first video on the list is played automatically when the `VideoListActivity` is loaded

### Databinding
Used on activities' layouts

### LiveData and MediatorLiveData
To load information from both API and Room

### App Executor
Used to load Room data in a background thread



## ScreenShots:
![List Screen](https://i.ibb.co/685kd4m/listscreen.png)
![Details Screen](https://i.ibb.co/0jPKSZ5/device-2019-07-04-202206.png)
![Video Screen](https://i.ibb.co/KXCNBZG/device-2019-07-04-202224.png)
