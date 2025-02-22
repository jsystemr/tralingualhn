/**
 * 
 */
 setTimeout(function () {
		      $(".alert").fadeTo(2000, 500).slideUp(500, function () {
		          $(".alert-danger").remove();
		      });
		    }, 3000);//5000=5 seconds
