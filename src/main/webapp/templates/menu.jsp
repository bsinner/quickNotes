<!-- Menu bar -->
<div class="ui huge menu">
    <div class="header item">
        <a href="editor" id="homeLink">Quick Notes</a>
    </div>
    <div class="right menu" id="rightMenu">
        <a href="#" class="item" id="signIn">Sign In</a>
        <a href="#" class="item">Sign Up</a>
    </div>
</div>

<!-- Login modal -->
<div class="ui tiny modal" id="loginModal">

    <i class="icon-cancel" id="loginExit"></i>
    <div class="header">
        Sign In
    </div>

    <div class="content">

        <form class="ui form" id="loginForm">

            <div class="field">
                <label>E-mail</label>
                <input type="text" id="email" name="email">
            </div>

            <div class="field">
                <label>Password</label>
                <input type="password" id="password" name="password">
            </div>

        </form>

        <button class="ui button" id="loginBtn">Sign In</button>

    </div>
</div>

<!-- Create modal -->
<div class="ui tiny modal" id="createModal">

    <i class="icon-cancel" id="createExit"></i>
    <div class="header">
        Create
    </div>

    <div class="content">

        <form class="ui form">

            <div class="field" id="titleDiv">
                <div class="ui error message">
                    <p>Note must have a title</p>
                </div>
                <label>Title</label>
                <input id="title" type="text" maxlength="40">
            </div>

        </form>

        <br>
        <button class="ui right floated green button leftButtonMargin" id="createSubmit">Create</button>
        <button class="ui right floated button" id="createCancel">Cancel</button>
        <br><br>

    </div>
</div>

