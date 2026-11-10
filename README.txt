MI BOLSILLO 3.0 - PROYECTO ANDROID
==================================
Funciones: Personal/Negocio, vistas diario/mensual/rango, análisis,
Descubre, retos, 5 monedas, CSV, informes imprimibles o PDF,
notificación opcional de registro rápido y widget Android configurable.

COMPILAR DESDE GITHUB EN EL CELULAR
1. Copia todos los archivos del ZIP a la raíz de tu repositorio privado MiBolsillo.
2. Haz commit y push a main.
3. En GitHub, abre Actions > Compilar APK Mi Bolsillo.
4. Abre la ejecución verde, baja a Artifacts y descarga MiBolsillo-APK-debug.
5. Extrae el ZIP del artifact e instala app-debug.apk.

ACTUALIZAR DESDE TERMUX (si ya clonaste el repositorio):
  cd ~/MiBolsillo
  unzip -o ~/storage/downloads/MiBolsilloAndroid_v3_proyecto.zip -d ~/mb_v3
  cp -r ~/mb_v3/MiBolsilloAndroid/. ~/MiBolsillo/
  git add .
  git commit -m "Mi Bolsillo 3.0: informes, widget y notificaciones"
  git push origin main

ATENCION AL ACTUALIZAR LA APP
La compilación de DEBUG en GitHub Actions usa normalmente una firma temporal
que puede CAMBIAR entre ejecuciones. Si Android rechaza la actualización,
no desinstales sin antes EXPORTAR UN RESPALDO JSON desde la versión vieja.
Después desinstala la vieja, instala la nueva e importa ese JSON.
Para actualizaciones de producción se necesita una clave de firma estable.

MONEDAS
BOB, USD, EUR, BRL y ARS. Cada cuenta tiene una moneda de presentación.
Cambiarla NO convierte importes, NO calcula tipos de cambio. El historial
existente conserva sus cifras originales sin ninguna conversión automática.
No mezclar importes de distintas monedas en una misma cuenta.

NOTIFICACIONES
En Ajustes puedes activar una notificación fija opcional. Al tocarla se
abre el formulario de gasto. En Android 13+ se pide permiso de notificaciones.
El widget se agrega desde la pantalla de inicio de Android, opción Widgets,
Mi Bolsillo. Elige Personal o Negocio al instalar el widget.

PREMIUM Y ANUNCIOS
NO hay anuncios activos ni cobros en esta compilación. La interfaz incluye
una sección de Premium pendiente de publicar, sin activar falsas compras.
Antes de monetizar es NECESARIO integrar y probar Google Play Billing con
un producto no consumible, verificar compras, configurar AdMob y consentimiento,
y entregar la ausencia de anuncios a compradores Premium verificados.
NO publicitar como compra operativa la sección que todavía está pendiente.

GOOGLE PLAY
La APK debug es para pruebas, no publicación. Para Play se requiere AAB
firmado para producción, target SDK vigente, declaraciones y política de
privacidad, políticas de datos, pruebas cerradas cuando correspondan.

Datos guardados en WebView localStorage. No se sincronizan con otros teléfonos.
Exporta respaldos JSON con regularidad.
