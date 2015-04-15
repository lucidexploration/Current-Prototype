CREATE TABLE accounts (
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    email VARCHAR(40) NOT NULL UNIQUE,
    hint VARCHAR(20),
    PRIMARY KEY(username)
);

CREATE TABLE characters (
    username VARCHAR(20) NOT NULL,
    charactername VARCHAR(20) NOT NULL UNIQUE,
    PRIMARY KEY(username,charactername)
);

CREATE TABLE characterinfo (
    charactername VARCHAR(20) NOT NULL UNIQUE,
    hp INT NOT NULL,
    hpmax INT NOT NULL,
    mp INT NOT NULL,
    mpmax INT NOT NULL,
    strength INT NOT NULL,
    speed INT NOT NULL,
    intelligence INT NOT NULL,
    wisdom INT NOT NULL,
    agility INT NOT NULL,
    currentexperience INT NOT NULL,
    totalexperience INT NOT NULL,
    PRIMARY KEY(charactername)
);

CREATE TABLE characterlocation (
    charactername VARCHAR(20) NOT NULL UNIQUE,
    x INT NOT NULL,
    y INT NOT NULL,
    z INT NOT NULL,
    PRIMARY KEY(charactername)
);

CREATE TABLE characteritems (
    charactername VARCHAR(20) NOT NULL,
    itemid INT NOT NULL UNIQUE,
    PRIMARY KEY(charactername, itemid)
);

CREATE TABLE items(
    itemid INT NOT NULL UNIQUE,
    itemdamage INT NOT NULL,
    itemtype INT NOT NULL,
    PRIMARY KEY(itemid)
);

CREATE TABLE characterfriends (
    username VARCHAR(20) NOT NULL,
    charactername VARCHAR(20) NOT NULL,
    PRIMARY KEY(username, charactername)
);

CREATE TABLE guilds (
    guildname VARCHAR(20) NOT NULL UNIQUE,
    guildleader VARCHAR(20) NOT NULL UNIQUE,
    guilddesc VARCHAR(20),
    PRIMARY KEY(guildname)
);

CREATE TABLE guildmembers (
    guildname VARCHAR(20) NOT NULL,
    charactername VARCHAR(20) NOT NULL UNIQUE,
    PRIMARY KEY(guildname,charactername)
);