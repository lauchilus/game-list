-- Tabla played
CREATE TABLE played (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  game_id INT,
  FOREIGN KEY (user_id) REFERENCES users(id)
  
);

-- Tabla playing
CREATE TABLE playing (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  name VARCHAR(255),
  description VARCHAR(255),
  image VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla collections
CREATE TABLE collections (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  nombre VARCHAR(255),
  descripcion VARCHAR(255),
  image VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla games
CREATE TABLE games (
  id INT AUTO_INCREMENT PRIMARY KEY,
  game_id INT,
  collection_id INT,
  FOREIGN KEY (collection_id) REFERENCES collections(id)
);
