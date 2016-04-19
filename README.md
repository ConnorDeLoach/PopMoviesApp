# PopMoviesApp
Android app to find popular movies.

Need to supply your own themoviedb api_key found here - https://www.themoviedb.org/

API key needs to be inserted in MainFragment.java
Under MyAsyncClass search for: 

Uri builtUri = Uri.parse(params[0]).buildUpon()
                    .appendQueryParameter(APPID_PARAM, "YOUR_API_KEY")
                    .build();

Replace "YOUR_API_KEY" with your themoviesdb API key
