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
                <label>Title</label>
                <input id="title" type="text" maxlength="40">
                <div class="ui compact small negative message" id="titleError" style="display: none;">
                    <p>Note must have title</p>
                </div>
            </div>

        </form>

        <br>
        <button class="ui right floated green button leftButtonMargin" id="createSubmit">Create</button>
        <button class="ui right floated button" id="createCancel">Cancel</button>
        <br><br>

    </div>
</div>

<!-- Translate modal -->
<div class="ui tiny modal" id="translateModal">

    <i class="icon-cancel" id="translateEdit"></i>
    <div class="header">
        Translate
    </div>

    <div class="content">

        <form class="ui form">

            <div class="field">
                <label>Source Language</label>
                <select id="sourceLang" class="ui fluid dropdown">
                    <%@include file="langDropdown.jsp"%>
                </select>
            </div>

            <div class="field">
                <label>Destination Language</label>
                <select id="destLang" class="ui fluid dropdown">
                    <%@include file="langDropdownDest.jsp"%>
                </select>
            </div>

        </form>

        <br>
        <button class="ui right floated green button leftButtonMargin" id="translateSubmit">Translate</button>
        <button class="ui right floated button" id="translateCancel">Cancel</button>
        <br><br>

    </div>

</div>

