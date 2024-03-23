import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vitejs.dev/config/
export default defineConfig({
        plugins: [react()],
        server: {
            host: "localhost",
            proxy: {
                "^/api/*": "http://localhost:8080"
            },
            port: 5174
        }
    }
)
