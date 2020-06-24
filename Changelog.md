# Flutter Changelog

# 0.4.4

 - Support added for the latest Netflix app.
 - Open imdb button added.

# 0.4.3

 - Support added for Disney+ app.

# 0.4.2

 - Upgrade target SDK to 28.
 - Migrate to AndroidX.
 - Migrate to Firebase Analytics and optimize events.

# 0.4.1

 - Support page layout and persuasion message updated

# 0.4.0

 - No changes

# 0.3.9

 - Update PrimeVideo reader to support the new UI

# 0.3.8

 - Fix issue with Toast. Removed custom duration for toasts
 - Fixed support page layout and card height
 - Fixed issue of not retaining 'liked' state in collection search
 - 'Draw over window' banner added
 - 'Detailed' rating bubble added
 - 'Not found' bubble added

# 0.3.7

 - Debug menu added
 - NetflixReaderService refactored
 - PendingIntent notification issue fixed
 - Battery optimization / whitelist info screen, settings section added
 - Settings page refactored
 - Notification settings page and section added
 - Update banners mechanism added
 - Banners added about MAL, Redbox app, and battery optimizations
 - Push notification content updated

# 0.3.6

 - In-app purchase card's purchase status fixed
 - Movie rating expiry time added (2 weeks)
 - Privacy policy added in the app
 - Support added for My Anime List

# 0.3.5

 - Bottom navigation integrated
 - Support page card designs updated
 - Search page design updated
 - Info page options updated
 - Fix bug where movie page would not open correctly
 - Option added to choose order of the movie rating - popular or recent
 - Rate app / Support app notification threshold decreased
 - Accessibility page info updated
 - Report bug option added
 - Fixed bug where moshi would not parse "N/A" to int

# 0.3.4

 - Made copies more generic instead of being about movies
 - Redux architecture added
 - Moshi and code-gen integrated
 - Separated db and networking entities
 - GA events added for ratings
 - Made some GA events non-interactive

# 0.3.3

 - `Trending` page added
 - Events cleaned up
 - Movie list jitters fixed
 - GA integrated
 - Progress loader added to support page
 - Empty collection list page share issue fixed

# 0.3.2

 - Fix `NoSuchElement` crash

### 0.3.1

 - Make less API calls
 - Handle and store 404 errors
 - Catch IllegalStateException error
 - Recycle nodes after the usage

### 0.3.0

 - Users can open movie details page directly from Flutter
 - Settings navigation issue fixed
 - Flutter's private API to get accurate movie ratings
 - Fixed Netflix year (accessibility) search

### 0.2.9

 - Updated compile and target sdk to 27
 - Updated gradle tools to 3.1.3
 - Configure position and color of the rating bubble
 - text styles added and lint rules added to keep a check
 - Retry action added if the network fails
 - Support and rate app notification and history keeper added

### 0.2.8

 - Support added for BBC iPlayer, Hotstar, Jio TV, Jio Cinema

### 0.2.7

 - Dialog added for locale not supported
 - In-app purchases tags updated
 - Cleaned-up settings page and created different sections
 - Functionality added to export your data offline and import it again
 - Similarly, the same can be used to share complete app data with other users
 - FileProvider added to share exported files through other apps
 - Intent filters added to catch intents for ACTION_SEND and ACTION_VIEW for 'text/plain' type
 - Share collection feature added

### 0.2.6

 - Fix crash for in-app billing services
 - Google Play Movies & TV app support added
 - App info page merged with search page empty state

### 0.2.5

 - In-app purchases added for 'donations'
 - Node recycle bugfix
 - Fix like toggle
 - Changed text and icon colors to improve contrast

### 0.2.3

 - NPE bugfixes in Accessibility Services
 - Fix activity not found issues
 - Fix volume slider issue

### 0.2.2

 - Fix crash if Text to Speech is not supported by the device
 - Auto hide floating rating window
 - Hide floating window if the movie has changed
 - Relatively better onboarding

### 0.2.1

 - Text to Speech support added
 - Crashyltics and Fabric disabled
 - 'Add to collection' shortcut support added
 - Bugfixes and memory optimizations
 - More options added to settings
 - Some refactoring around search adapter and base movie list fragment

### 0.2.0

 - Detailed episode page added
 - Open Imdb page menu action added

### 0.1.9

 - When showing ratings for movies on Netlfix and Amazon Prime, parse year of the release and
 use it to get accurate ratings.
 - Pagination added to search results. Now, you can search for more than 12 results.
 - List of episodes and seasons added on the page for a TV series.

### 0.1.8

 - Support added for Amazon Prime Video.
 - Options added to delete user data.

### 0.1.7

 - Movie collections added.
 - Fixed issue where recent history showed incorrect data (likes) about the movies.

### 0.1.6

 - Fixed bug for Android O where it does not show the rating window.
 - Added Recently Viewed (History) section.

### 0.1.5

 - Movie search added.
 - You can add a movie to your favorites and check it out later.
 - Movie page added where you can get more details about the movie.