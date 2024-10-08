import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vitejs.dev/config/
export default defineConfig({
        plugins: [react()],
        base: "/admin/",
        server: {
            host: "localhost",
            proxy: {
                "^/api/*": "http://localhost:8080"
            }
        }
    }
)
