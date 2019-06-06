CREATE TABLE [Artikal]
( 
	[IdArtikal]          integer  NOT NULL  IDENTITY ( 1,1 ) ,
	[Ime]                varchar(100)  NULL ,
	[Cena]               decimal(10,3)  NULL 
	CONSTRAINT [pozitivna_vrednost_402377763]
		CHECK  ( Cena >= 0 ),
	[Kolicina]           integer  NULL 
	CONSTRAINT [pozitivna_vrednost_2073741418]
		CHECK  ( Kolicina >= 0 ),
	[IdProdavnica]       integer  NOT NULL 
)
go

ALTER TABLE [Artikal]
	ADD CONSTRAINT [XPKArtikal] PRIMARY KEY  CLUSTERED ([IdArtikal] ASC)
go

CREATE TABLE [Grad]
( 
	[IdGrad]             integer  NOT NULL  IDENTITY ( 1,1 ) ,
	[Ime]                varchar(100)  NULL 
)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdGrad] ASC)
go

CREATE TABLE [Kupac]
( 
	[IdKupac]            integer  NOT NULL  IDENTITY ( 1,1 ) ,
	[Ime]                varchar(100)  NULL ,
	[novac]              integer  NULL 
	CONSTRAINT [pozitivna_vrednost_436142360]
		CHECK  ( novac >= 0 ),
	[IdGrad]             integer  NULL 
)
go

ALTER TABLE [Kupac]
	ADD CONSTRAINT [XPKKupac] PRIMARY KEY  CLUSTERED ([IdKupac] ASC)
go

CREATE TABLE [Linija]
( 
	[udaljenost]         integer  NULL 
	CONSTRAINT [pozitivna_vrednost_1755208265]
		CHECK  ( udaljenost >= 0 ),
	[IdGrad2]            integer  NOT NULL ,
	[IdGrad1]            integer  NOT NULL 
)
go

ALTER TABLE [Linija]
	ADD CONSTRAINT [XPKLinija] PRIMARY KEY  CLUSTERED ([IdGrad2] ASC,[IdGrad1] ASC)
go

CREATE TABLE [NaruceniArtikal]
( 
	[IdPorudzbina]       integer  NOT NULL ,
	[IdArtikal]          integer  NOT NULL ,
	[Kolicina]           integer  NOT NULL  IDENTITY ( 1,1 ) 
	CONSTRAINT [pozitivna_vrednost_1262884424]
		CHECK  ( Kolicina >= 0 ),
	[IdNarArtikal]       char(18)  NOT NULL 
)
go

ALTER TABLE [NaruceniArtikal]
	ADD CONSTRAINT [XPKNaruceniArtikal] PRIMARY KEY  CLUSTERED ([IdNarArtikal] ASC)
go

CREATE TABLE [Porudzbina]
( 
	[IdPorudzbina]       integer  NOT NULL  IDENTITY ( 1,1 ) ,
	[Stanje]             varchar(100)  NULL ,
	[IdKupac]            integer  NOT NULL ,
	[datum_sent]         datetime  NULL ,
	[datum_received]     datetime  NULL ,
	[Lokacija]           integer  NULL 
)
go

ALTER TABLE [Porudzbina]
	ADD CONSTRAINT [XPKPorudzbina] PRIMARY KEY  CLUSTERED ([IdPorudzbina] ASC)
go

CREATE TABLE [Prodavnica]
( 
	[IdProdavnica]       integer  NOT NULL  IDENTITY ( 1,1 ) ,
	[popust]             integer  NULL 
	CONSTRAINT [pozitivna_vrednost_1754553136]
		CHECK  ( popust >= 0 ),
	[novac]              decimal(10,3)  NULL 
	CONSTRAINT [pozitivna_vrednost_2123801444]
		CHECK  ( novac >= 0 ),
	[IdGrad]             integer  NOT NULL 
)
go

ALTER TABLE [Prodavnica]
	ADD CONSTRAINT [XPKProdavnica] PRIMARY KEY  CLUSTERED ([IdProdavnica] ASC)
go

CREATE TABLE [Profit]
( 
	[idProfit]           integer  NOT NULL ,
	[profit]             decimal(10,3)  NULL 
)
go

ALTER TABLE [Profit]
	ADD CONSTRAINT [XPKProfit] PRIMARY KEY  CLUSTERED ([idProfit] ASC)
go

CREATE TABLE [Putanja]
( 
	[IdPorudzbina]       integer  NOT NULL ,
	[datumDolaska]       datetime  NULL ,
	[IdGrad]             integer  NOT NULL 
)
go

ALTER TABLE [Putanja]
	ADD CONSTRAINT [XPKPutanja] PRIMARY KEY  CLUSTERED ([IdPorudzbina] ASC,[IdGrad] ASC)
go

CREATE TABLE [Time_Operations]
( 
	[idGeneral]          integer  NOT NULL ,
	[current_time]       date  NULL 
)
go

ALTER TABLE [Time_Operations]
	ADD CONSTRAINT [XPKTime_Operations] PRIMARY KEY  CLUSTERED ([idGeneral] ASC)
go

CREATE TABLE [Transakcija]
( 
	[IdTransakcija]      integer  NOT NULL ,
	[Time]               datetime  NULL ,
	[Iznos]              decimal(10,3)  NULL 
	CONSTRAINT [pozitivna_vrednost_1004713968]
		CHECK  ( Iznos >= 0 ),
	[IdKupac]            integer  NOT NULL ,
	[IdPorudzbina]       integer  NOT NULL ,
	[IdProdavnica]       integer  NULL 
)
go

ALTER TABLE [Transakcija]
	ADD CONSTRAINT [XPKTransakcijaKupca] PRIMARY KEY  CLUSTERED ([IdTransakcija] ASC)
go


ALTER TABLE [Artikal]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([IdProdavnica]) REFERENCES [Prodavnica]([IdProdavnica])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Kupac]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Linija]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IdGrad2]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Linija]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IdGrad1]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [NaruceniArtikal]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([IdPorudzbina]) REFERENCES [Porudzbina]([IdPorudzbina])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [NaruceniArtikal]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([IdArtikal]) REFERENCES [Artikal]([IdArtikal])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Porudzbina]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([IdKupac]) REFERENCES [Kupac]([IdKupac])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Porudzbina]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([Lokacija]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Prodavnica]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Putanja]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([IdPorudzbina]) REFERENCES [Porudzbina]([IdPorudzbina])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Putanja]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Transakcija]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([IdKupac]) REFERENCES [Kupac]([IdKupac])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Transakcija]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IdPorudzbina]) REFERENCES [Porudzbina]([IdPorudzbina])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Transakcija]
	ADD CONSTRAINT [R_16] FOREIGN KEY ([IdProdavnica]) REFERENCES [Prodavnica]([IdProdavnica])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
