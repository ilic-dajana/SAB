CREATE PROCEDURE dbo.addArtikal
    @idPorudzbine int,
    @idArtikal int,
	@count int,
	@idNarArtikl int OUTPUT
AS
BEGIN
     IF EXISTS ( SELECT * FROM Artikal WHERE IdArtikal = @idArtikal) AND EXISTS (SELECT * FROM Porudzbina WHERE IdPorudzbina = @idPorudzbine)
	BEGIN
		INSERT INTO NaruceniArtikal(IdPorudzbina, IdArtikal, Kolicina) VALUES(@idPorudzbine, @idArtikal, @count)
		SET @idNarArtikl = SCOPE_IDENTITY()
	END
	ELSE 
		SET @idNarArtikl = -1;
		
END

go

CREATE PROCEDURE dbo.removeArtikal
    @orderId int ,
    @articalId int,
	@status int OUTPUT
AS
BEGIN
	IF EXISTS (SELECT * FROM NaruceniArtikal WHERE IdPorudzbina = @orderId AND IdArtikal = @articalId)
	BEGIN
		DELETE FROM NaruceniArtikal WHERE IdPorudzbina = @orderId AND IdArtikal = @articalId
		SET @status = 1
	END
	ELSE
		SET @status = -1
END

go

CREATE PROCEDURE dbo.CalculateDiscount 
    @orderId int ,
    @discount decimal(10,3) OUTPUT 
AS
BEGIN
	IF EXISTS (SELECT IdPorudzbina FROM Porudzbina WHERE IdPorudzbina = @orderId)
	BEGIN
		declare @imapopust ints

		exec SP_IMA_DODATNI_POPUST @orderId, @imapopust out

		SELECT @discount = SUM(N.Kolicina * A.Cena * (P.popust + @imapopust)/ 100)
		FROM NaruceniArtikal AS N
		INNER JOIN  Artikal AS A ON N.IdArtikal = A.IdArtikal
		INNER JOIN  Prodavnica AS P ON A.IdProdavnica = P.IdProdavnica
		WHERE N.IdPorudzbina = @orderId
	END
	ELSE
		SET @discount = -1;
END

go

CREATE PROCEDURE dbo.SP_FINAL_PRICE
	@orderId int,
    @racun  decimal(10,3) OUTPUT 
AS
BEGIN
	IF EXISTS (SELECT IdPorudzbina FROM Porudzbina WHERE IdPorudzbina = @orderId)
	BEGIN
		declare @discount  DECIMAL(10,3)
		exec dbo.CalculateDiscount @orderId , @discount OUTPUT
		
		SELECT @racun = SUM(N.Kolicina * A.Cena ) - @discount
		FROM NaruceniArtikal AS N
		INNER JOIN  Artikal AS A ON N.IdArtikal = A.IdArtikal
		INNER JOIN  Prodavnica AS P ON A.IdProdavnica = P.IdProdavnica
		WHERE N.IdPorudzbina = @orderId
	END
	ELSE
		SET @racun = -1;
END

go

ALTER PROCEDURE dbo.Gradovi_porudzbine
    @orderId int     
AS
BEGIN
	
		SELECT P.IdGrad
		FROM NaruceniArtikal AS N
		INNER JOIN Artikal AS A ON A.IdArtikal = N.IdArtikal
		INNER JOIN Prodavnica AS P ON P.IdProdavnica = A.IdProdavnica
		WHERE N.IdPorudzbina = @orderId

END

go

CREATE PROCEDURE dbo.Grad_korisnik
    @orderId int   
AS
	IF EXISTS (SELECT Porudzbina.IdPorudzbina FROM Porudzbina WHERE IdPorudzbina = @orderId)
	begin
		SELECT K.IdGrad
		FROM Porudzbina P, Kupac K
		WHERE P.IdPorudzbina = @orderId AND K.IdKupac = P.IdKupac 
	end
	else
	begin
		select -1;
	end
go

CREATE PROCEDURE dbo.Time_initial
	@date DATE
AS
BEGIN
	IF NOT EXISTS(SELECT * FROM Time_Operations)
	BEGIN
		INSERT INTO Time_Operations(IdGeneral, [current_time]) VALUES (1, @date)
	END
	ELSE
	BEGIN
		UPDATE Time_Operations set [current_time] = @date WHERE idGeneral = 1
	END
END
go

CREATE PROCEDURE dbo.SP_IMA_DODATNI_POPUST
    @idOrder int = 0,
    @imapopust int OUTPUT 
AS
begin
    declare @tekuciDatum DATE
	declare @idKupac int
	
	select @tekuciDatum = [current_time]
	from Time_Operations
	where idGeneral = 0

	select @idKupac = idKupac 
	from Porudzbina
	where idPorudzbina = @idOrder

	if exists (select *
				from Transakcija AS T 
				INNER JOIN Porudzbina AS P ON T.idPorudzbina = P.idPorudzbina
				where P.idPorudzbina <> @idOrder and P.datum_received >= DATEADD(DAY, -30, @tekuciDatum)
				group by P.idPorudzbina
				having sum(T.Iznos)>10000)
			set @imapopust = 2
	else
			set @imapopust = 0

end		
go

CREATE TRIGGER TR_TRANSFER_MONEY_TO_SHOPS
    ON Porudzbina
    FOR UPDATE
    AS
    BEGIN
     declare @cursorI CURSOR
	 declare @cursorD CURSOR
	 declare @idPorD int
	 declare @idPorI int
	 declare @idStanjeD varchar(100)
	 declare @idStanjeI varchar(100)

	 SET @cursor = CURSOR FOR
	 SELECT Stanje, IdPorudzbina
	 from inserted

	 SET @cursor = CURSOR FOR
	 SELECT Stanje, IdPorudzbina
	 from deleted

	 open @cursorI
	 open @cursorD

	 fetch next from @cursorI
	 into @idPorI, @idStanjeI
	 fetch next from @cursorD
	 into @idPorD, @idStanjeD

	 while(@@FETCH_STATUS = 0)
	 begin
		IF(@idStanjeD = 'sent' and @idStanjeI= 'arrived')
		begin
			declare @suma decimal(10,3)


		end


		 fetch next from @cursorI
		 into @idPorI, @idStanjeI
		 fetch next from @cursorD
		 into @idPorD, @idStanjeD
	 end


	 close @cursorI
	 close @cursorD

	 deallocate @cursorI
	 deallocate @cursorD

    END
