INSERT INTO `visionlogistica`.`tiposdemovimientosmanifiestosfacturas` (`nombreTipoDeMovimiento`, `activo`, `fechaIng`, `usuario`) VALUES ('RECOGIDAS', '1', '2014-03-31 22:00:00', 'lopez164');
 
CREATE TABLE IF NOT EXISTS `visionlogistica`.`recogidaspormanifiesto` (
  `idRecogidasPorManifiesto` INT NOT NULL,
  `idNumeroManifiesto` INT NOT NULL,
  `numeroFactura` VARCHAR(12) NOT NULL,
  `facturaAfectada` VARCHAR(12) NOT NULL,
  `numeroDeSoporte` VARCHAR(12) NOT NULL,
  `valorRecogida` DOUBLE NOT NULL,
  `activo` INT NOT NULL,
  `fechaIng` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario` VARCHAR(45) NOT NULL,
  `flag` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`idrecogidaspormanifiesto`),
  UNIQUE INDEX `recogidaspormanifiesto_idx` (`idNumeroManifiesto` ASC, `numeroFactura` ASC),
  CONSTRAINT `fk_recogidaspormanifiesto_1`
    FOREIGN KEY (`idrecogidaspormanifiesto`)
    REFERENCES `visionlogistica`.`facturaspormanifiesto` (`consecutivo`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB




  ALTER TABLE `visionlogistica`.`manifiestosdedistribucion` 
        ADD COLUMN `valorRecogida` DOUBLE NOT NULL DEFAULT 0 AFTER `valorRecaudado`;

 ALTER TABLE `visionlogistica`.`facturascamdun` 
        ADD COLUMN `valorRecogida` DOUBLE NOT NULL DEFAULT 0 AFTER `valorDescuento`;


/***********************************************************************************************************************************************+++*/
/*MODIFICACION DE LA VISTA BBDD REMOTA  07-XI-2015 */
USE `visionlogistica`;
CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    DEFINER = `luislopez`@`%` 
    SQL SECURITY DEFINER
VIEW `vst_defintivofacturaspormanifiesto` AS
    SELECT 
        `facturaspormanifiesto`.`adherencia` AS `adherencia`,
        `facturaspormanifiesto`.`numeroManifiesto` AS `numeroManifiesto`,
        `manifiestosdedistribucion`.`fechaDistribucion` AS `fechaDistribucion`,
        `manifiestosdedistribucion`.`vehiculo` AS `vehiculo`,
        `manifiestosdedistribucion`.`conductor` AS `conductor`,
        CONCAT(`personas`.`nombres`,
                ' ',
                `personas`.`apellidos`) AS `nombreConductor`,
        `manifiestosdedistribucion`.`ruta` AS `idRuta`,
        `rutasdedistribucion`.`nombreDeRuta` as `nombreDeRuta`,
        `facturaspormanifiesto`.`numeroFactura` AS `numeroFactura`,
        `facturaspormanifiesto`.`valorARecaudarFactura` AS `valorARecaudarFactura`,
        `facturaspormanifiesto`.`fechaIng` AS `fechaIng`,
        `facturascamdun`.`fechaDeVenta` AS `fechaDeVenta`,
        `facturascamdun`.`cliente` AS `cliente`,
        `clientescamdun`.`nombreDeCliente` AS `nombreDeCliente`,
        `clientescamdun`.`direccion` AS `direccionDeCliente`,
        `clientescamdun`.`barrio` AS `barrio`,
        `clientescamdun`.`celularCliente` AS `telefonoCliente`,
        `facturascamdun`.`vendedor` AS `vendedor`,
        `facturascamdun`.`formaDePago` AS `formaDePago`,
        `manifiestosdedistribucion`.`canal` AS `idCanal`,
        `tiposcanaldeventas`.`nombreCanalDeVenta` as `nombreCanal`,
        `facturascamdun`.`valorFacturaSinIva` AS `valorFacturaSinIva`,
        `facturascamdun`.`valorIvaFactura` AS `valorIvaFactura`,
        `facturascamdun`.`valorTotalFactura` AS `valorTotalFactura`,
        `facturascamdun`.`valorRechazo` AS `valorRechazo`,
        `facturascamdun`.`valorDescuento` AS `valorDescuento`,
        `facturascamdun`.`valorTotalRecaudado` AS `valorTotalRecaudado`
    FROM
        ((((((`facturaspormanifiesto`
        JOIN `facturascamdun`)
        JOIN `clientescamdun`)
        JOIN `manifiestosdedistribucion`)
        JOIN `personas`)
        JOIN `rutasdedistribucion`)
        JOIN `tiposcanaldeventas`)
    WHERE
        ((`facturaspormanifiesto`.`numeroFactura` = `facturascamdun`.`numeroFactura`)
            AND (`facturaspormanifiesto`.`numeroManifiesto` = `manifiestosdedistribucion`.`consecutivo`)
            AND (`manifiestosdedistribucion`.`conductor` = `personas`.`cedula`)
            AND (`manifiestosdedistribucion`.`ruta` = `rutasdedistribucion`.`idRuta`)
            AND (`manifiestosdedistribucion`.`canal` = `tiposcanaldeventas`.`idCanalDeVenta`)
            AND (`clientescamdun`.`codigoInterno` = `facturascamdun`.`cliente`));

/*************************************************************************************************************************************************/
/* MODIFICACION  A LA BASE DE DDATOS SE CREAN DOS TABLAS :
1. FACTURAS ANULADAS
2. BITACORAFACTURAS 
3. Y SE CREAN SUS RESPECTIVOS TRIGGERS EN LA BBDD 
15 DE NOVIEMBRE DE 2015 12:00 */

--
-- Table structure for table `bitacorafacturas`
--

DROP TABLE IF EXISTS `bitacorafacturas`;

CREATE TABLE `bitacorafacturas` (
  `idbitacorafacturas` int(11) NOT NULL AUTO_INCREMENT,
  `numeroFactura` varchar(12) NOT NULL,
  `observacion` varchar(100) NOT NULL,
  `activo` int(11) NOT NULL DEFAULT '1',
  `fechaIng` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario` varchar(45) NOT NULL,
  `flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idbitacorafacturas`),
  KEY `fk_bitacorafacturas_1_idx` (`numeroFactura`),
  CONSTRAINT `fk_bitacorafacturas_1` FOREIGN KEY (`numeroFactura`) REFERENCES `facturascamdun` (`numeroFactura`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


--
-- Table structure for table `facturasanuladas`
--
CREATE TABLE IF NOT EXISTS `mydb`.`facturasanuladas` (
  `idfacturasanuladas` INT NOT NULL AUTO_INCREMENT,
  `idControlador` INT NOT NULL,
  `numeroFactura` VARCHAR(12) NOT NULL,
  `numeroManifiesto` INT NOT NULL,
  `activo` INT NOT NULL DEFAULT 1,
  `fechaIng` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario` VARCHAR(45) NOT NULL,
  `flag` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`idfacturasanuladas`),
  INDEX `fk_facturasanuladas_1_idx` (`numeroFactura` ASC),
  UNIQUE INDEX `index2` (`numeroFactura` ASC),
  INDEX `fk_facturasanuladas_2_idx` (`idControlador` ASC),
  CONSTRAINT `fk_facturasanuladas_1`
    FOREIGN KEY (`numeroFactura`)
    REFERENCES `visionlogistica`.`facturasCamdun` (`numeroFactura`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_facturasanuladas_2`
    FOREIGN KEY (`idControlador`)
    REFERENCES `visionlogistica`.`controladordedocumentos` (`idcontrolador`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
ENGINE = InnoDB DEFAULT CHARSET=utf8;


CREATE TRIGGER `visionlogistica`.`facturasanuladas_AFTER_INSERT` AFTER INSERT ON `facturasanuladas` FOR EACH ROW
BEGIN

INSERT INTO `bitacorafacturas` (`numeroFactura`, `observacion`, 
`usuario`) 
 VALUES (NEW.numeroFactura, CONCAT("FACTURA ANULADA POR  ",NEW.observacion) , 
 NEW.usuario); 
   
END 

CREATE TRIGGER `visionlogistica`.`facturascamdun_AFTER_INSERT` AFTER INSERT ON `facturascamdun` FOR EACH ROW
BEGIN

INSERT INTO `bitacorafacturas` ( `numeroFactura`,`observacion`, 
 `usuario`) 
 VALUES (NEW.numeroFactura, "FACTURA INGRESADA AL SISTEMA ", 
 NEW.usuario); 
   

END 


CREATE TRIGGER `visionlogistica`.`facturasdescargadas_AFTER_INSERT` 
AFTER INSERT ON `facturasdescargadas` FOR EACH ROW
BEGIN

DECLARE nombreCausalRec varchar(45) DEFAULT 0;
DECLARE valor varchar(45) DEFAULT 0;

select nombreCausalDeRechazo into nombreCausalRec
FROM causalesderechazo 
WHERE 
idcausalesDeRechazo = NEW.motivoRechazo;
  
-- SE VALIDA EL TIPO DE NOVIMIENTO
 
  CASE NEW.movimientoFactura
  
  -- SE INSERTA EL DATO 
  
      WHEN '1' THEN set valor="";
	  WHEN '2' THEN set valor="ENTREGA TOTAL ";
      WHEN '3' THEN set valor=CONCAT("RECHAZO TOTAL POR ",nombreCausalRec);
      WHEN '4' THEN 
        IF(NEW.motivoRechazo=1) THEN
		    SET valor="ENTREGA CON NOVEDAD, DESCUENTO "; 
		ELSE 
		     SET valor=CONCAT("RECHAZO PARCIAL POR ",nombreCausalRec) ;
		END IF;
      
   
  END CASE;
 INSERT INTO `bitacorafacturas` (`numeroFactura`, `observacion`,
 `usuario`) 
 VALUES (NEW.numeroFactura, valor,
 NEW.usuario); 
   
END

--
-- Table structure for table `facturaspormanifiesto`
--

CREATE TRIGGER `visionlogistica`.`facturaspormanifiesto_AFTER_INSERT` AFTER INSERT ON `facturaspormanifiesto` FOR EACH ROW
BEGIN
DECLARE carro varchar(45) DEFAULT 0;


SELECT vehiculo into carro
 FROM manifiestosdedistribucion
 WHERE
 consecutivo=NEW.numeroManifiesto;

INSERT INTO `bitacorafacturas` (`numeroFactura`,`observacion`,
 `usuario`) 
 VALUES (NEW.numeroFactura, CONCAT("FACTURA SALE A DISTRIBCION EN VEHICULO ",carro),
 NEW.usuario); 
 

END 

/*******************************************************************************************************************************************+*/
/* MODIFICACION  A LA BASE DE DDATOS SE CREAN DOS TABLAS :
1. CONTROLADOR DE DOCUMENTOS, EN LA CUAL HAY QUE CREAR TODOS LOS MANIFIESTOS DE DISTRIBUCION
   PARA QUE NO SE GENERE UN ERROR DE LLAVES FORANEAS EN LA TABLA MANIFIESTOS DE
   DISTRIBUCION
2. TIPOS DE DOCUMENTOS
**/
  


CREATE TABLE IF NOT EXISTS `controladordedocumentos` (
  `idcontrolador` INT NOT NULL AUTO_INCREMENT,
  `tipoDocumento` INT NOT NULL,
  `isFree` INT NOT NULL DEFAULT 1,
  `clave` VARCHAR(100) NOT NULL,
  `activo` INT NOT NULL DEFAULT 1,
  `fechaIng` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario` VARCHAR(45) NOT NULL,
  `flag` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`idcontrolador`),
  INDEX `fk_documentosdistribucion_1_idx` (`tipoDocumento` ASC),
  CONSTRAINT `Fk_controladordedocumentos_1`
    FOREIGN KEY (`tipoDocumento`)
    REFERENCES `visionlogistica`.`tiposdedocumentos` (`idtipoDeDocumento`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `visionlogistica`.`tiposdedocumentos` (
  `idtipoDeDocumento` INT NOT NULL AUTO_INCREMENT,
  `nombreDeDocumento` VARCHAR(100) NOT NULL,
  `activo` INT NOT NULL DEFAULT 1,
  `fechaIng` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario` VARCHAR(45) NOT NULL,
  `flag` INT NOT NULL DEFAULT 1,,
  PRIMARY KEY (`idtipoDeDocumento`))
ENGINE = InnoDB



/**************************************************************************************************************************************************/
-- 1. copia de seguridad tabla manifiestos de distribucion.
-- 2. sentencia sql pára llenar controlador de los manifeistos:

use visionlogistica;
drop table if EXISTS facturasanuladas;
drop table if EXISTS controladordedocumentos;
drop table if EXISTS facturasparaanular ;
drop table if EXISTS bitacorafacturas ;

CREATE TABLE IF NOT EXISTS `bitacorafacturas` (
  `idbitacorafacturas` INT NOT NULL AUTO_INCREMENT,
  `documento` VARCHAR(20) NULL,
  `numeroFactura` VARCHAR(11) NOT NULL,
  `movimiento` VARCHAR(200) NOT NULL,
  `activo` INT NOT NULL DEFAULT 1,
  `usuario` VARCHAR(45) NOT NULL,
  `fechaIng` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `flag` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`idbitacorafacturas`),
  INDEX `fk_bitacorafacturas_1_idx` (`numeroFactura` ASC),
  CONSTRAINT `fk_bitacorafacturas_1`
    FOREIGN KEY (`numeroFactura`)
    REFERENCES `visionlogistica`.`facturascamdun` (`numeroFactura`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `bitacorafacturas` 
(`documento`, `numeroFactura`, `movimiento`, `activo`, `usuario`, `fechaIng`, `flag`) 
SELECT '',numeroFactura,'INGRESADO AL SISTEMA',activo,usuario,fechaIng,flag
from facturascamdun order by fechaIng asc;

INSERT INTO `bitacorafacturas` 
(`documento`, `numeroFactura`, `movimiento`, `activo`, `usuario`, `fechaIng`, `flag`) 
SELECT facturaspormanifiesto.numeroManifiesto,facturaspormanifiesto.numeroFactura,
concat('SALIENDO A DISTRIBUCION EN VEHICULO : ',manifiestosdedistribucion.vehiculo),
facturaspormanifiesto.activo,facturaspormanifiesto.usuario,facturaspormanifiesto.fechaIng,facturaspormanifiesto.flag
from facturaspormanifiesto,manifiestosdedistribucion
where
manifiestosdedistribucion.consecutivo=facturaspormanifiesto.numeroManifiesto 
order by facturaspormanifiesto.fechaIng asc;

INSERT INTO `bitacorafacturas` 
(`documento`, `numeroFactura`, `movimiento`, `activo`, `usuario`, `fechaIng`, `flag`) 
SELECT facturasdescargadas.numeroManifiesto,facturasdescargadas.numeroFactura,
if(facturasdescargadas.movimientoFactura=2,'ENTREGADO EN RUTA',
if(facturasdescargadas.movimientoFactura=3,CONCAT('DEVUELTO DE RUTA POR :',causalesderechazo.nombreCausalDeRechazo
),'RECHAZO PARCIAL O ENTREGA CON NOVEDAD')),
facturasdescargadas.activo,facturasdescargadas.usuario,facturasdescargadas.fechaIng,facturasdescargadas.flag
from facturasdescargadas,causalesderechazo 
where
 facturasdescargadas.motivorechazo=causalesderechazo.idcausalesDeRechazo
 order by facturasdescargadas.fechaIng asc;
 
 update facturaspormanifiesto
  join facturascamdun
  on facturascamdun.numeroFactura=facturaspormanifiesto.numeroFactura
  set facturaspormanifiesto.valorARecaudarFactura=facturascamdun.valorTotalFactura;


CREATE TABLE IF NOT EXISTS `controladordedocumentos` (
  `idcontrolador` INT NOT NULL AUTO_INCREMENT,
  `tipoDocumento` INT NOT NULL,
  `isFree` INT NOT NULL DEFAULT 1,
  `clave` VARCHAR(100) NOT NULL,
  `activo` INT NOT NULL DEFAULT 1,
  `fechaIng` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario` VARCHAR(45) NOT NULL,
  `flag` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`idcontrolador`),
  INDEX `fk_documentosdistribucion_1_idx` (`tipoDocumento` ASC),
  CONSTRAINT `Fk_controladordedocumentos_1`
    FOREIGN KEY (`tipoDocumento`)
    REFERENCES `visionlogistica`.`tiposdedocumentos` (`idtipoDeDocumento`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB DEFAULT CHARSET=utf8;



INSERT INTO `controladordedocumentos`
(`idcontrolador`,
`tipoDocumento`,
`isFree`,
`clave`,
`usuario`,
`fechaIng`
)
select consecutivo,1,1,'nananana',usuario,fechaIng from manifiestosdedistribucion
order by consecutivo asc;

-- 3. modificar la tabla manifiestos de distribucion.

ALTER TABLE `visionlogistica`.`manifiestosdedistribucion` 
ADD CONSTRAINT `fk_manifiestosdedistribucion_7`
  FOREIGN KEY (`consecutivo`)
  REFERENCES `visionlogistica`.`controladordedocumentos` (`idcontrolador`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

-- 4. 
INSERT INTO `visionlogistica`.`estadosdemanifiesto` (`idEstadoManifiesto`, `nombreDeEstadoManifiesto`,`usuario`) VALUES ('5', 'ANULADO','lopez164');
UPDATE `visionlogistica`.`estadosdemanifiesto` SET `nombreDeEstadoManifiesto`='DESCARGADO' WHERE `idEstadoManifiesto`='4';

-- 5. 
update visionlogistica.manifiestosdedistribucion set estadoManifiesto=5
where estadoManifiesto =  2;

-- 6. 
ALTER TABLE `visionlogistica`.`facturaspormanifiesto` 
DROP FOREIGN KEY `fk_facturasPorManifiesto_1`;

-- 7. 
ALTER TABLE `visionlogistica`.`facturaspormanifiesto` 
ADD CONSTRAINT `fk_facturaspormanifiesto_1`
  FOREIGN KEY (`numeroManifiesto`)
  REFERENCES `visionlogistica`.`controladordedocumentos` (`idcontrolador`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

-- 8

CREATE TABLE IF NOT EXISTS `facturasanuladas` (
  `idfacturasanuladas` INT NOT NULL AUTO_INCREMENT,
  `numeroManifiesto` INT NOT NULL,
  `numeroFactura` VARCHAR(12) NOT NULL,
  `observacion` VARCHAR(150) NOT NULL,
  `activo` INT NOT NULL DEFAULT 1,
  `fechaIng` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario` VARCHAR(45) NOT NULL,
  `flag` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`idfacturasanuladas`),
  INDEX `fk_facturasanuladas_1_idx` (`numeroFactura` ASC),
  UNIQUE INDEX `index2` (`numeroFactura` ASC),
  INDEX `fk_facturasanuladas_2_idx` (`numeroManifiesto` ASC),
  CONSTRAINT `fk_facturasanuladas_1`
    FOREIGN KEY (`numeroFactura`)
    REFERENCES `visionlogistica`.`facturascamdun` (`numeroFactura`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_facturasanuladas_2`
    FOREIGN KEY (`numeroManifiesto`)
    REFERENCES `visionlogistica`.`controladordedocumentos` (`idcontrolador`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB DEFAULT CHARSET=utf8;

-- 9. 

CREATE TABLE IF NOT EXISTS `facturasparaanular` (
  `numeroFactura` VARCHAR(12) NOT NULL,
  `causalDeRechazo` VARCHAR(100) NOT NULL,
  `activo` INT NOT NULL DEFAULT 1,
  `fechaIng` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario` VARCHAR(45) NOT NULL,
  `flag` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`numeroFactura`))
ENGINE = InnoDB DEFAULT CHARSET=utf8;