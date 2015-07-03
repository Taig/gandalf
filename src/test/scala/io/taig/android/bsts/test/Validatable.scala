package io.taig.android.bsts.test

import android.os.Build.VERSION_CODES.LOLLIPOP
import android.view.View
import android.widget.{EditText, FrameLayout}
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R
import io.taig.android.bsts.rule.string.Required
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers, RobolectricSuite}

@Config( sdk = Array( LOLLIPOP ) )
class	Validatable
extends	FlatSpec
with	Matchers
with	BeforeAndAfterEach
with	RobolectricSuite
{
	implicit val context = RuntimeEnvironment.application

	"A View (without validation rules)" should "validate to true" in
	{
		new View( context ).validate() shouldBe true
	}

	"A ViewGroup (without validation rules)" should "validate to true if it has no children" in
	{
		new FrameLayout( context ).validate() shouldBe true
	}

	it should "validate to true if none of its children have validation rules" in
	{
		val parent = new FrameLayout( context )
		parent.addView( new View( context ) )
		parent.addView( new View( context ) )
		parent.addView( new View( context ) )

		parent.validate() shouldBe true
	}

	it should "fail if one of its children does not pass validation" in
	{
		val parent = new FrameLayout( context )

		val editText = new EditText( context )

		parent.addView( new View( context ) )
		parent.addView( editText )
		parent.addView( new View( context ) )

		editText obeys Required()

		editText.validate() shouldBe false
		parent.validate() shouldBe false
	}

	it should "succeed if all its children pass validation" in
	{
		val parent = new FrameLayout( context )

		val editText1 = new EditText( context )
		editText1.setText( "a" )

		val editText2 = new EditText( context )
		editText2.setText( "b" )

		parent.addView( editText1 )
		parent.addView( new View( context ) )
		parent.addView( editText2 )

		editText1 obeys Required()
		editText2 obeys Required()

		editText1.validate() shouldBe true
		editText2.validate() shouldBe true
		parent.validate() shouldBe true
	}

	it should "work with deeply nested validation children" in
	{
		val parentParent = new FrameLayout( context )
		val parent = new FrameLayout( context )

		val editText1 = new EditText( context )
		editText1.setText( "a" )

		val editText2 = new EditText( context )
		editText2.setText( "b" )

		parentParent.addView( parent )
		parentParent.addView( new FrameLayout( context ) )
		parent.addView( new FrameLayout( context ) )
		parent.addView( editText1 )
		parent.addView( new View( context ) )
		parent.addView( editText2 )

		editText1 obeys Required()
		editText2 obeys Required()

		editText1.validate() shouldBe true
		editText2.validate() shouldBe true
		parentParent.validate() shouldBe true

		editText1.setText( "" )
		editText1.validate() shouldBe false
		parentParent.validate() shouldBe false
	}

	it should "be possible to clear all error messages" in
	{
		val parent = new FrameLayout( context )

		val editText1 = new EditText( context )
		val editText2 = new EditText( context )

		parent.addView( editText1 )
		parent.addView( new View( context ) )
		parent.addView( editText2 )

		editText1 obeys Required()
		editText2 obeys Required()

		parent.validate()

		editText1.getError shouldBe context.getString( R.string.validation_string_required )
		editText2.getError shouldBe context.getString( R.string.validation_string_required )

		parent.clear()

		editText1.getError shouldBe null
		editText2.getError shouldBe null
	}
}