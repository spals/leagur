package webservice

import (
	"testing"
	"net/url"
	"fmt"
	"net/http"
	"net/http/httptest"
)

func TestFoo(t *testing.T) {
	response := httptest.NewRecorder()

	apiUrl := "https://api.com"
	resource := "/user/"
	data := url.Values{}
	data.Set("id", "id")
	data.Set("entity", "entity")
	data.Set("key", "key")

	u, _ := url.ParseRequestURI(apiUrl)
	u.Path = resource
	u.RawQuery = data.Encode()
	urlStr := fmt.Sprintf("%v", u)
	fmt.Printf("hello world")
	request, _ := http.NewRequest("POST", urlStr, nil)

	Handler(response, request)
}