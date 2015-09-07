package main

import (
	"github.com/spals/leagur/webservice"
	"net/http"
)

func main() {
	http.HandlerFunc("/", webservice.Handler)
	http.ListenAndServe(":8080", nil)
}