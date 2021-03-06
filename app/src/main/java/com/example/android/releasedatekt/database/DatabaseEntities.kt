package com.example.android.releasedatekt.database

import androidx.room.*
import com.example.android.releasedatekt.domain.Genre
import com.example.android.releasedatekt.domain.Movie
import com.example.android.releasedatekt.domain.MovieTrailer
import java.util.*

@Entity
data class DatabaseMovie constructor(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Int,
    @ColumnInfo(name = "page")
    val page: Int,
    @ColumnInfo(name = "popularity")
    val popularity: Float,
    @ColumnInfo(name = "vote_count")
    val voteCount: Int,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Float,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: Long?
)

@Entity
data class DatabaseGenre constructor(
    @PrimaryKey
    @ColumnInfo(name = "genre_id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String
)

@Entity
data class DatabaseMovieTrailer constructor(
    @PrimaryKey
    @ColumnInfo(name = "video_id")
    val id: Int,
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "key")
    val key: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "site")
    val site: String,
    @ColumnInfo(name = "size")
    val size: Int,
    @ColumnInfo(name = "type")
    val type: String
)

@Entity(primaryKeys = ["movie_id", "genre_id"])
data class DatabaseMovieGenreCrossRef(
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "genre_id", index = true)
    val genreId: Int
)

data class DatabaseMovieWithGenres(
    @Embedded
    val databaseMovie: DatabaseMovie,
    @Relation(
        parentColumn = "movie_id",
        entityColumn = "genre_id",
        associateBy = Junction(DatabaseMovieGenreCrossRef::class)
    )
    val genres: List<DatabaseGenre>
)

fun DatabaseMovieTrailer.asDomainModelMovieTrailer(): MovieTrailer {
    return MovieTrailer(
        id = id,
        movieId = movieId,
        key = key,
        name = name,
        site = site,
        size = size,
        type = type
    )
}

fun DatabaseMovieWithGenres.asDomainModelMovie(): Movie {
    return Movie(
        id = databaseMovie.id,
        popularity = databaseMovie.popularity,
        voteCount = databaseMovie.voteCount,
        posterPath = databaseMovie.posterPath,
        language = databaseMovie.language,
        title = databaseMovie.title,
        genres = genres.map { it.asDomainModelGenres() },
        voteAverage = databaseMovie.voteAverage,
        overview = databaseMovie.overview,
        releaseDate = databaseMovie.releaseDate?.let {
            Date(it)
        }
    )
}

fun DatabaseGenre.asDomainModelGenres(): Genre {
    return Genre(
        id = id,
        name = name
    )
}