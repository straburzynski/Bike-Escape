INSERT INTO Difficulty (Id, Name) VALUES (1, 'easy');
INSERT INTO Difficulty (Id, Name) VALUES (2, 'hard');
INSERT INTO Difficulty (Id, Name) VALUES (3, 'medium');
ALTER SEQUENCE difficulty_id_seq START WITH 4;

INSERT INTO RaceType (Id, Name) VALUES (1, 'tasks');
INSERT INTO RaceType (Id, Name) VALUES (2, 'time');
ALTER SEQUENCE racetype_id_seq START WITH 3;

INSERT INTO Users (Id, Email, FirstName, Password) VALUES (1, 'admin', 'Sebastian', '$2a$12$2HMB8PtYhTp4Z4racNjQS.3.Z9NI5tyegXZkA4ZeUmNR7BSliOgFO');
ALTER SEQUENCE users_id_seq START WITH 2;

INSERT INTO Authority (Id, Name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO Authority (Id, Name) VALUES (2, 'ROLE_USER');
ALTER SEQUENCE authority_id_seq START WITH 3;

INSERT INTO UserAuthority (UserId, AuthorityId) VALUES (1, 1);
INSERT INTO UserAuthority (UserId, AuthorityId) VALUES (1, 2);

