let refToRedirect;
let formId;
function initWork() {
    const sampleForm = document.getElementById(formId);
    sampleForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        let form = e.currentTarget;
        let url = form.action;
        try {
            let formData = new FormData(form);
            let responseData = await postFormFieldsAsJson({url, formData});
            let {serverDataResponse} = responseData;
            console.log(serverDataResponse);
        } catch (error) {
            console.error(error);
        }
    });

    async function postFormFieldsAsJson({url, formData}) {
        let formDataObject = Object.fromEntries(formData.entries());

        let formDataJsonString = JSON.stringify(formDataObject);
        let fetchOptions = {
            method: "POST",
            //Set the headers that specify you're sending a JSON body request and accepting JSON response
            headers: {
                "Content-Type": "application/json",
                Accept: "text/html",
            },

            body: formDataJsonString,
        };

        await fetch(url, fetchOptions).then((res) => {
            if (res.ok) window.location.href = refToRedirect;
        });
    }
}
