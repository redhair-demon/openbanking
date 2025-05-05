CREATE TABLE accounts(
    id                                         UUID NOT NULL PRIMARY KEY,
    status                                            VARCHAR(7) NOT NULL,
    status_updated_at                              TIMESTAMP NOT NULL,
    currency                                          VARCHAR(3) NOT NULL,
    account_type                                       VARCHAR(8) NOT NULL,
    account_description                                TEXT NOT NULL,
    owner_id bigint references owners not null ,
    servicer_id bigint references servicers not null
);

CREATE TABLE account_details(
    account_id                         UUID references accounts PRIMARY KEY,
    name                               VARCHAR(13) NOT NULL,
    identification                     INTEGER  references identifications
);

CREATE TABLE servicers(
    id BIGINT PRIMARY KEY,
    name                                      VARCHAR(45) NOT NULL,
    bank_identification INTEGER references identifications,
    organization_identification INTEGER references identifications,
    correspondent_account_identification INTEGER references identifications,
    postal_address_id bigint references postal_address
);

CREATE TABLE owners(
    id bigint PRIMARY KEY,
    identification INTEGER references identifications,
    name                                         VARCHAR(45) NOT NULL,
    mobileNumber                                 INTEGER  NOT NULL,
    countryOfResidence                           VARCHAR(2) NOT NULL,
    countryOfBirth                               VARCHAR(2) NOT NULL,
    provinceOfBirth                              VARCHAR(19) NOT NULL,
    cityOfBirth                                  VARCHAR(10) NOT NULL,
    birthDate                                    VARCHAR(25) NOT NULL,
    postal_address_id bigint references postal_address
);

CREATE TABLE postal_address(
    id bigint PRIMARY KEY,
    addressType                     VARCHAR(6),
    addressLines                    TEXT[],
    streetName                      VARCHAR(7),
    buildingNumber                  INTEGER ,
    postCode                        INTEGER ,
    townName                        VARCHAR(7),
    countrySubDivision              VARCHAR(8),
    country                         VARCHAR(2)
);

CREATE TABLE identifications(
    schemeName                    VARCHAR(11) NOT NULL,
    identification                INTEGER PRIMARY KEY
);