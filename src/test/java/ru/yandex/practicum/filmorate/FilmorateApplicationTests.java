package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmLikeDao;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserService userService;
	private final FilmService filmService;
	private final MpaService mpaService;
	private final GenreService genreService;
	private final FilmLikeDao filmLikeDao;

	@Test
	void testAddUserShouldNamePetr() {
		createPetr();
		Optional<User> optionalUser = Optional.of(userService.getUserById(1));
		assertThat(optionalUser)
				.isPresent()
				.hasValueSatisfying(user1 -> assertThat(user1).hasFieldOrPropertyWithValue("name", "Petr"));

	}

	@Test
	void testGetUsersShouldSize2() {
		createPetr();
		createPetr2();

		assertThat(userService.readAll()).hasSize(2);
	}

	@Test
	void testGetFriendsShouldSize2() {
		User user1 = createPetr();
		User user2 = createPetr2();
		User user3 = createPetr3();
		userService.addFriend(user1.getId(), user2.getId());

		userService.addFriend(user1.getId(), user3.getId());
		assertThat(userService.getFriendsForUserId(1)).hasSize(2);
	}

	@Test
	void testGetCommonFriendsShouldId1() {
		User user = createPetr();

		User user2 = createPetr2();

		User user3 = createPetr3();

		userService.addFriend(user2.getId(), user.getId());
		userService.addFriend(user3.getId(), user.getId());
		assertThat(userService.getCommonFriends(2, 3)).hasSize(1);
	}

	@Test
	void testDeleteFriendShouldIsEmpty() {
		User user = createPetr();

		User user2 = createPetr2();

		userService.addFriend(user.getId(), user2.getId());
		assertThat(userService.getFriendsForUserId(1)).hasSize(1);
		userService.deleteFriend(user.getId(), user2.getId());
		assertThat(userService.getFriendsForUserId(1)).isEmpty();
	}

	@Test
	void testUpdateUserShouldNamePetr() {
		User user = createPetr();

		user.setName("Petr22");
		userService.update(user);
		assertThat(userService.getUserById(1).getName()).isEqualTo("Petr22");
	}

	@Test
	void testGetFilmsShouldSize2() {
		Mpa mpa = mpaService.getMpaById(1);
		createFilm(mpa);
		createFilm2(mpa);
		assertThat(filmService.readAll()).hasSize(2);
	}

	@Test
	void testGetFilmShouldTitleFilm() {
		Mpa mpa = mpaService.getMpaById(1);
		createFilm(mpa);
		assertThat(filmService.getFilmById(1).getName()).isEqualTo("Film");
	}

	@Test
	void testGetPopularFilmsShouldSize2() {
		Mpa mpa = mpaService.getMpaById(1);
		createFilm(mpa);
		createFilm2(mpa);
		assertThat(filmService.getPopularFilms(2)).hasSize(2);
	}

	@Test
	void testCreateFilmShouldTitleFilm() {
		Mpa mpa = mpaService.getMpaById(1);
		createFilm(mpa);
		assertThat(filmService.getFilmById(1).getName()).isEqualTo("Film");
	}

	@Test
	void testUpdateFilmShouldTitleOtherFilm() {
		Mpa mpa = mpaService.getMpaById(1);
		Film film = createFilm(mpa);
		film.setName("Film22");
		filmService.update(film);
		assertThat(filmService.getFilmById(1).getName()).isEqualTo("Film22");
	}

	@Test
	void testUserLikeFilmShouldCount1() {
		createPetr();

		Mpa mpa = mpaService.getMpaById(1);
		createFilm(mpa);

		filmService.addLike(userService.getUserById(1).getId(), filmService.getFilmById(1).getId());
		assertThat(filmLikeDao.readLikesByFilmId(1)).hasSize(1);
	}

	@Test
	void testGetAllRatingsCount5() {
		assertThat(mpaService.readAll()).hasSize(5);
	}

	@Test
	void testGetRatingsById() {
		assertThat(mpaService.getMpaById(1)).hasFieldOrPropertyWithValue("name", "G");
		assertThat(mpaService.getMpaById(2)).hasFieldOrPropertyWithValue("name", "PG");
		assertThat(mpaService.getMpaById(3)).hasFieldOrPropertyWithValue("name", "PG-13");
		assertThat(mpaService.getMpaById(4)).hasFieldOrPropertyWithValue("name", "R");
		assertThat(mpaService.getMpaById(5)).hasFieldOrPropertyWithValue("name", "NC-17");
	}

	@Test
	void testGetGenresCount6() {
		assertThat(genreService.readAll()).hasSize(6);
	}

	@Test
	void testGetGenresById() {
		assertThat(genreService.getGenreById(1)).hasFieldOrPropertyWithValue("name", "Комедия");
		assertThat(genreService.getGenreById(2)).hasFieldOrPropertyWithValue("name", "Драма");
		assertThat(genreService.getGenreById(3)).hasFieldOrPropertyWithValue("name", "Мультфильм");
		assertThat(genreService.getGenreById(4)).hasFieldOrPropertyWithValue("name", "Триллер");
		assertThat(genreService.getGenreById(5)).hasFieldOrPropertyWithValue("name", "Документальный");
		assertThat(genreService.getGenreById(6)).hasFieldOrPropertyWithValue("name", "Боевик");
	}

	private User createPetr() {
		User user = new User().toBuilder()
				.name("Petr")
				.login("petr21")
				.email("p21@mail.ru")
				.birthday(LocalDate.of(1990, 1, 1))
				.build();
		return userService.create(user);
	}

	private User createPetr2() {
		User user2 = new User().toBuilder()
				.name("Petr2")
				.login("petr212")
				.email("p212@mail.ru")
				.birthday(LocalDate.of(1990, 1, 1))
				.build();
		return userService.create(user2);
	}

	private User createPetr3() {
		User user3 = new User().toBuilder()
				.name("Petr23")
				.login("petr2123")
				.email("p2123@mail.ru")
				.birthday(LocalDate.of(1990, 1, 1))
				.build();
		return userService.create(user3);
	}

	private Film createFilm(Mpa mpa) {
		Film film = new Film().toBuilder()
				.name("Film")
				.description("abc")
				.duration(150)
				.releaseDate(LocalDate.of(1999, 1, 1))
				.mpa(mpa)
				.build();
		return filmService.create(film);
	}

	private Film createFilm2(Mpa mpa) {
		Film film2 = new Film().toBuilder()
				.name("Film2")
				.description("abc")
				.duration(150)
				.releaseDate(LocalDate.of(1999, 1, 1))
				.mpa(mpa)
				.build();
		return filmService.create(film2);
	}

}
