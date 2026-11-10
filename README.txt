MI BOLSILLO - PROYECTO ANDROID

Este ZIP contiene el proyecto fuente, NO un APK ya compilado.

PARA CREAR EL APK EN ANDROID STUDIO:
1. Extrae el ZIP.
2. Abre Android Studio > Open y selecciona la carpeta MiBolsilloAndroid.
3. Instala Android SDK 35 y JDK 17 si Android Studio lo solicita.
4. Espera a que Gradle sincronice el proyecto. Requiere Internet para descargar herramientas por primera vez.
5. Menú Build > Build Bundle(s) / APK(s) > Build APK(s).
6. Encontrarás el APK de depuración en: app/build/outputs/apk/debug/app-debug.apk
7. Copia ese APK a tu teléfono e instálalo.

NOTAS:
- No requiere conexión a Internet para el funcionamiento normal.
- Guarda gastos, metas y movimientos dentro del almacenamiento local de Android WebView.
- Para copiar datos desde el HTML anterior, usa Ajustes > Exportar respaldo JSON en la versión HTML e importa el JSON en la APK. El almacenamiento no se transfiere solo.
- Los respaldos JSON/CSV usan el selector de documentos del sistema Android.
- El dictado usa la app de reconocimiento de voz disponible en tu teléfono.
- No se ha compilado ni probado físicamente este proyecto en este entorno.
- No borres datos de la app sin hacer una copia JSON.

OPCION ALTERNATIVA: GITHUB ACTIONS (SIN INSTALAR ANDROID STUDIO)
1. Crea un repositorio GitHub y sube el contenido de la carpeta MiBolsilloAndroid (no la carpeta contenedora).
2. Abre Actions > Compilar APK Mi Bolsillo > Run workflow.
3. Al terminar, descarga el archivo MiBolsillo-APK-debug de la sección Artifacts.
4. Dentro está app-debug.apk para Android.

Este procedimiento requiere cuenta GitHub, Internet y permiso para ejecutar workflows.
