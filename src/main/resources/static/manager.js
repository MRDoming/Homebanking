const app = Vue.createApp({
    data() {
        return {
            url: '/rest/clients',
            urlAnt: "",
            urlSig: "",
            datosCompletos: {},
            datos: [],
            cliente: { firstName: "", lastName: "", email: "" },
            totalClientes: 0,
            page: 0,
            clienteAEditar: {},
            editCliente: { firstName: "", lastName: "", email: "" },
        }
    },
    created() {
        this.loadData(this.url)
    },
    mounted() {
    },
    methods: {
        loadData(url) {
            axios
                .get(url)
                .then(response => {
                    this.datosCompletos = response
                    this.datos = this.datosCompletos.data._embedded.clients
                    this.totalClientes = this.datosCompletos.data.page.totalElements
                    this.page = this.datosCompletos.data.page
                    this.urlAnt = ""
                    if (this.datosCompletos.data._links.prev) {
                        this.urlAnt = this.datosCompletos.data._links.prev.href
                    }
                    this.urlSig = ""
                    if (this.datosCompletos.data._links.next) {
                        this.urlSig = this.datosCompletos.data._links.next.href
                    }
                })

            .catch(error => console.log(error))
        },
        postClient(url, newClient) {
            axios.post(url, newClient)
            //.then(this.loadData(this.url))
            .catch(error => console.log(error))
        },
        addClient() {
            if (this.cliente.firstName != "" && this.cliente.lastName != "" && (this.cliente.email != "" && this.cliente.email.includes("@"))) {
                this.postClient(this.url, this.cliente)
            }
        },
        clientePEditar(cliente){
            this.clienteAEditar = cliente
        },
        editarCliente(){
            if(this.editCliente.firstName == ""){
                this.editCliente.firstName = this.clienteAEditar.firstName
            }
            if(this.editCliente.lastName == ""){
                this.editCliente.lastName = this.clienteAEditar.lastName
            }
            if(this.editCliente.email == ""){
                this.editCliente.email = this.clienteAEditar.email
            }
            axios.put(this.clienteAEditar._links.self.href, this.editCliente)
            .catch(error => console.log(error))
        },
        borrar(cliente) {
            axios.delete(cliente._links.self.href)
            .catch(error => console.log(error))
        }
    },
    computed: {

    }
}).mount('#app')