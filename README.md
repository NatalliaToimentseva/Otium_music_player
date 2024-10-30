# Otium music player

Diploma project: audio player works both with internal storage and API.<br />
## Tech stack:<br />
UI - Jetpack Compose, <br />
Architecture - Clean Architecture, UI layer - MVI;<br />
Navigation - Jetpack Navigation Component, <br />
Dependency injection - Hilt;<br />
Asynchrony - coroutines and flow;<br />
Network -Retrofit2, Glide, Coil-compose, parser - GSON<br />
DataBase - Room;<br />
Pagination - Paging-compose3 library;<br />
Permissions - Accompanist Permissions;<br />
Andriod components: foreground services - MediaSessionService; BroadcastReceiver; ContentProvider.<br />
Libraries - media3Ui, media3Session, player - media3Exoplayer.<br />

## 1. Navigation (Look at pic. 1 Navigation)

![pic  1 Navigaion](https://github.com/user-attachments/assets/8a5105a4-23bd-4565-86b5-7d6df77d042e)

Navigation is represented by NavigationBar.<br />
The first tab - playlists (stored in the database);<br />
The second and third tabs are internal storage data; <br />
The fourth tab is a Tab layout with a View Pager.<br />
The All tab is a search from the Jamendo API with recommendations displayed in LazyRow, the Favorites tab is a LazyColumn from the database.<br />

### 1.1 Functionality and package path<br />
 #### 1.1.1 CollectionListScreen(path: com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main)<br />
 Functionality: <br />
	- add a playlists<br />
	- rename a playlists<br />
	- delete a playlist<br />
 #### 1.1.2 PlaylistTracksScreen(path: com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks)<br />
 Functionality: <br />
	- clicking on a track takes you to the player<br />
	- delete tracks from playlist<br />
 #### 1.1.3 FoldersScreen(path: com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.main)<br />
 Functionality: <br />
	- clicking on a folder takes to folders tracks<br />
 #### 1.1.4 FoldersTracksScreen (path: com.example.otiummusicplayer.ui.features.mobileStoragePart.folders.folderTracks) <br />
Functionality: <br />
	- clicking on a track takes you to the player<br />
	- add a track to a playlist<br />
 #### 1.1.5 MobileStorageTracksScreen (path: com.example.otiummusicplayer.ui.features.mobileStoragePart.allTracks)<br />
Functionality: <br />
	- clicking on a track takes you to the player<br />
	- add a  to a playlist<br />
 #### 1.1.6 NetworkSearchScreen (path: com.example.otiummusicplayer.ui.features.networkPart.mainScreen)<br />
Functionality: <br />
	- container for TabRow and HorizontalPager<br />
	- Tab Favorite shows TracksListElement - LazyColumn of tracks stored in the database<br />
 #### 1.1.7 AllDataScreenElement (path: com.example.otiummusicplayer.ui.features.networkPart.mainScreen.screenElements)<br />
Functionality: <br />
	- contains SearchFieldScreenElement for network search. Search result is TracksListElement - LazyColumn of tracks returned by the API.<br />
	- conteiner for AlbumsList, ArtistsList, ShowArtistsAlbums<br />
 #### 1.1.8 AlbumsList (path: com.example.otiummusicplayer.ui.features.networkPart.mainScreen.screenElements)<br />
Functionality: <br />
	- LazyRow of recomended albums<br />
 #### 1.1.9 ArtistsList (path: com.example.otiummusicplayer.ui.features.networkPart.mainScreen.screenElements)<br />
Functionality: <br />
	- LazyRow of recomended artists<br />
 #### 1.1.10 ShowArtistsAlbums (path: com.example.otiummusicplayer.ui.features.networkPart.mainScreen.screenElements)<br />
Functionality: <br />
	- ModalBottomSheet of artists albums<br />
 #### 1.1.11 TrackListScreen (path: com.example.otiummusicplayer.ui.features.networkPart.tracksScreen)<br />
Functionality: <br />
	- LazyColumn of tracks<br />
	- Clicking on a track takes you to the player<br />
 #### 1.1.12 PlayTrackScreen (path: com.example.otiummusicplayer.ui.features.playerControlScreen)<br />
Functionality: <br />
	- both with AudioPlayerControls displays the interface for playing the current track and controlling the player<br />
	
## 2. DataBase<br /> 
There are two tables:<br />
	- PlaylistsEntity for playlists of mobile storage part<br />
	- TracksDbEntity is used to store tracks for both playlist of mobile storage part and<br /> a favorite tracks retrieved from the network:<br />
		- The "isFavorite" column is used to store tracks received over the network.<br />
	- The "playlistId" column is used to display whether the track is in a playlist (the value "-1" is used for tracks that are not in the playlist).<br />
			
## 3. Models<br />
TrackModel - this is the main track model that is used by the domain layer:<br />
	TrackModel -> TracksDbEntity<br />
TracksDbEntity - this is the database model of track:<br />
	TracksDbEntity -> TrackModel<br />
MobileStorageTrackModel - this is a model for mobile stored tracks that are obtained using contentResolver:<br />
	MobileStorageTrackModel -> TrackModel<br />
TrackDataResponse - this is the main network responce model:<br />
	TrackDataResponse -> TrackModel<br />

## 3. Service (Look at pic. 2 Service)<br />
There are 2 service:<br />
	- DownloadManager for downloading track by network (path: com.example.otiummusicplayer.appComponents.services.downloadService)<br />
	- MediaSessionService for storing exoPlayer, mediaSessions and playing music in the background (path:com.example.otiummusicplayer.appComponents.services.musicService)<br />
 
The service stores mediaSession and exoPlayer objects. Starts working when connected to it from MediaPlayerController (path: com.example.otiummusicplayer.controllers):<br />

 ```kotlin
		private val browserFuture = MediaBrowser.Builder(context, token).buildAsync()
		    init {
        browserFuture.addListener({
            mediaBrowser = browserFuture.get()
            connectToServer()
            _isConnected.value = true
        }, MoreExecutors.directExecutor())
    }
```
MediaPlayerController is the class that the viewModel accesses to control the player, and it also implements Player.Listener interface to receive events such as:<br />
onMediaItemTransition and onIsPlayingChanged that are observed by the PlayerViewModel (path: com.example.otiummusicplayer.ui.features.playerControlScreen). <br />
PlayerViewModel is a viewModel of both PlayTrackScreen and AudioPlayerControls.<br />

MediaPlayerController also gets tracks for the exoPlayer playback queue from the PlayerViewModel and converts them into mediaItems:<br />

MediaPlayerController:<br />
 ```kotlin
	init {
        browserFuture.addListener({
            mediaBrowser = browserFuture.get()
            connectToServer()
            _isConnected.value = true
        }, MoreExecutors.directExecutor())
    }
```
then PlayerViewModel:<br />
 ```kotlin
	init {
        viewModelScope.launch(Dispatchers.Main) {
            serviceConnection.isConnected.collect { isConnected ->
                if (isConnected) {
                    state.value.tracks?.let { tracks ->
                        state.value.currentTrack?.let { track ->
                            serviceConnection.playAudio(tracks, track.id)
                        }
                    }
                }
            }
        }
```
then MediaPlayerController:<br />
 ```kotlin
	fun playAudio(tracks: List<TrackModel>, audioId: String) {
        audioList.value = tracks.toListMediaItem()
        val index =
            audioList.value.indexOf(audioList.value.find { it.mediaId == audioId })
        mediaController?.run {
            clearMediaItems()
            addMediaItems(audioList.value)
            prepare()
            seekTo(index, 0)
            playWhenReady = true
        }
    }
```
mediaSession and exoPlayer are created with help of DI - ServiceModule (path: com.example.otiummusicplayer.di)<br />
The interaction architecture is described in pic. 2<br />

![pic  2 Service ](https://github.com/user-attachments/assets/97d5be3f-cd0c-43ec-885f-2cd159a6a9a8)
