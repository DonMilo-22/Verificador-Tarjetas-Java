# Verificador de tarjetas en Java

Interfaz de escritorio que analiza **en tiempo real** el formato de los datos escritos. No hace cargos, no se conecta a bancos y no comprueba que una cuenta exista.

## Cómo abrirlo

Necesitas Java 17 o posterior. Abre `VerificadorTarjetas.jar` con Java o ejecuta desde una terminal en esta carpeta:

```bash
java -jar VerificadorTarjetas.jar
```

Si prefieres trabajar con el código fuente y tienes un JDK:

```bash
javac VerificadorTarjetas.java
java VerificadorTarjetas
```

En Windows, si al hacer doble clic no se abre, ejecuta `java -version` y después `java -jar VerificadorTarjetas.jar` en PowerShell para ver el error. El archivo `.jar` requiere Java instalado; no es un `.exe`.

## Prueba en clase

Activa **Mostrar datos simulados de los ejemplos** (viene activado) y escribe alguno de estos números de prueba publicados por Stripe:

| Número de prueba | Red | Emisor y tipo mostrados |
| --- | --- | --- |
| `4242 4242 4242 4242` | Visa | Banco Aula Norte / crédito **simulados** |
| `5555 5555 5555 4444` | Mastercard | Banco Aula Sur / débito **simulados** |

Escribe una fecha futura como `12/29` y un CVC ficticio de tres dígitos como `123`. Prueba cambiar el último dígito del número, poner una fecha vencida o borrar un dígito para ver los indicadores cambiar al instante. **Estos son números de prueba; los bancos y tipos mostrados son inventados únicamente para la demostración.** No introduzcas una tarjeta personal en una práctica de clase.

## Qué comprueba

1. Red Visa o Mastercard según los primeros dígitos.
2. Longitud admitida por esa red (Visa: 13, 16 o 19; Mastercard: 16).
3. Dígito de control mediante Luhn.
4. Fecha con mes válido, no vencida y no más de 25 años en el futuro.
5. CVC de tres dígitos para las dos redes implementadas (solo formato).

El banco emisor y el tipo de cuenta no se pueden inferir de forma fiable con estas reglas. Fuera de los dos ejemplos simulados, la interfaz indica que necesita un catálogo BIN actualizado. Tampoco se verifica si el CVC coincide con el registrado en el banco. El estado final dice **Formato válido**, nunca «tarjeta real».

La aplicación no almacena los datos en un archivo, no envía información por internet y no solicita autorizaciones de pago. Los datos permanecen en la memoria del programa mientras la ventana está abierta.

## Estructura

- `VerificadorTarjetas.java`: interfaz Swing y validaciones.
- `VerificadorTarjetas.jar`: versión ejecutable con Java 17+.

Referencias: [Visa, identificación de la red mediante IIN](https://design.visa.com/patterns/card-input/), [Visa, atributos de BIN](https://developer.visa.com/capabilities/visa-bin-attribute-sharing-service/faq) y [Stripe, números de prueba y Luhn](https://docs.stripe.com/testing).
