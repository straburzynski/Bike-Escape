CREATE TABLE IF NOT EXISTS authority
(
  id   SERIAL NOT NULL,
  name VARCHAR(65),
  CONSTRAINT authority_pkey
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS difficulty
(
  id   SERIAL       NOT NULL,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT difficulty_pkey
  PRIMARY KEY (id),
  CONSTRAINT uk_c0hl4naolclc8pykhlkmsqqcl
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS race
(
  id              VARCHAR(32)   NOT NULL,
  authorid        BIGINT,
  checkpoints     BIGINT,
  city            VARCHAR(255),
  created         TIMESTAMP,
  description     VARCHAR(2000) NOT NULL,
  estimatedtime   BIGINT,
  faildescription VARCHAR(2000) NOT NULL,
  isactive        BOOLEAN,
  ispublic        BOOLEAN,
  name            VARCHAR(255)  NOT NULL,
  summary         VARCHAR(2000) NOT NULL,
  difficultyid    INTEGER,
  racetypeid      INTEGER,
  CONSTRAINT race_pkey
  PRIMARY KEY (id),
  CONSTRAINT fk2rem2rmd5ibftwbji94llnoqa
  FOREIGN KEY (difficultyid) REFERENCES difficulty
);

CREATE TABLE IF NOT EXISTS racecheckpoint
(
  id          VARCHAR(32)   NOT NULL,
  answer      VARCHAR(255)  NOT NULL,
  answertype  VARCHAR(255)  NOT NULL,
  description VARCHAR(1000) NOT NULL,
  hint        VARCHAR(255)  NOT NULL,
  latitude    VARCHAR(255)  NOT NULL,
  longitude   VARCHAR(255)  NOT NULL,
  name        VARCHAR(255)  NOT NULL,
  number      BIGINT        NOT NULL,
  question    VARCHAR(255)  NOT NULL,
  raceid      VARCHAR(255)  NOT NULL,
  CONSTRAINT racecheckpoint_pkey
  PRIMARY KEY (id),
  CONSTRAINT fk82ai1o9j5mfwx5sjmmcldnheb
  FOREIGN KEY (raceid) REFERENCES race
);

CREATE TABLE IF NOT EXISTS racetype
(
  id   SERIAL       NOT NULL,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT racetype_pkey
  PRIMARY KEY (id),
  CONSTRAINT uk_isc4pah6i5kwd9dqhkadvl68n
  UNIQUE (name)
);

ALTER TABLE race
  ADD CONSTRAINT fke93v66drnvasqlovqeka6127j
FOREIGN KEY (racetypeid) REFERENCES racetype;

CREATE TABLE IF NOT EXISTS userauthority
(
  userid      BIGINT  NOT NULL,
  authorityid INTEGER NOT NULL,
  CONSTRAINT fkh400ib4qk4tu4addtjor37xj5
  FOREIGN KEY (authorityid) REFERENCES authority
);

CREATE TABLE IF NOT EXISTS userrace
(
  id         BIGSERIAL    NOT NULL,
  finishdate TIMESTAMP,
  racestatus VARCHAR(255) NOT NULL,
  totaltime  INTEGER,
  raceid     VARCHAR(255) NOT NULL,
  userid     BIGINT       NOT NULL,
  CONSTRAINT userrace_pkey
  PRIMARY KEY (id),
  CONSTRAINT fks8ic6y5i3219ll47l5w7hwksl
  FOREIGN KEY (raceid) REFERENCES race
);

CREATE TABLE IF NOT EXISTS users
(
  id        BIGSERIAL   NOT NULL,
  email     VARCHAR(255),
  firstname VARCHAR(65),
  password  VARCHAR(64) NOT NULL,
  CONSTRAINT users_pkey
  PRIMARY KEY (id),
  CONSTRAINT uk_jdfr6kjrxekx1j5vrr77rp44t
  UNIQUE (email)
);

ALTER TABLE userauthority
  ADD CONSTRAINT fkgjfp5j22vs5tmv7cc3usut4kh
FOREIGN KEY (userid) REFERENCES users;

ALTER TABLE userrace
  ADD CONSTRAINT fk3ypdfey4yrc8juc4v542r5mkb
FOREIGN KEY (userid) REFERENCES users;

CREATE TABLE IF NOT EXISTS usertoken
(
  userid BIGINT NOT NULL,
  token  VARCHAR(32),
  CONSTRAINT usertoken_pkey
  PRIMARY KEY (userid)
);

