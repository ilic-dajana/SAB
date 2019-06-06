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
		SELECT @discount = SUM(N.Kolicina * A.Cena * P.popust / 100)
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